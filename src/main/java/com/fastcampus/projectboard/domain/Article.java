package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = { // 특정 필드에 인덱스 추가로 검색 속도 향상
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 테이블의 PK로 사용할 id 필수

    // setter 를 각 필드에 지정해서 유연하게 변경 가능
    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000) private String content; // 내용 최대 10000자로 제한
    @Setter private String hashtag;

    @ToString.Exclude // ToString 에서 제외할 필드
    @OrderBy("id") // ArticleComment 를 id 기준으로 정렬
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // orphanRemoval = false 상태
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>(); // 중복 불허 (equals(), hashCode()로 판단), 순서 보장

    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자 이름 수 제한
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자 이름 수 제한

    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // 외부에서 아래 정적 팩토리 메소드(of)로 객체 생성 유도
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;

        if (this.getId() != null) {
            return Objects.equals(this.getId(), that.getId());
        } else {
            return Objects.equals(this.getTitle(), that.getTitle()) &&
                    Objects.equals(this.getContent(), that.getContent()) &&
                    Objects.equals(this.getHashtag(), that.getHashtag()) &&
                    Objects.equals(this.getCreatedAt(), that.getCreatedAt()) &&
                    Objects.equals(this.getCreatedBy(), that.getCreatedBy()) &&
                    Objects.equals(this.getModifiedAt(), that.getModifiedAt()) &&
                    Objects.equals(this.getModifiedBy(), that.getModifiedBy());
        }
    }

    @Override
    public int hashCode() {
        if (this.getId() != null) {
            return Objects.hash(getId());
        } else {
            return Objects.hash(getTitle(), getContent(), getHashtag(), getCreatedAt(), getCreatedBy(), getModifiedAt(), getModifiedBy());
        }
    }
}
