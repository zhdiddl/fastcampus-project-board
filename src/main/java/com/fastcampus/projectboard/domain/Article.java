package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = { // 특정 필드에 인덱스 추가로 검색 속도 향상
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 테이블의 PK로 사용할 id 필수

    @Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보 (ID)

    // setter 를 각 필드에 지정해서 유연하게 변경 가능
    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000) private String content; // 내용 최대 10000자로 제한
    @Setter private String hashtag;

    @ToString.Exclude // ToString 에서 제외할 필드
    @OrderBy("createdAt DESC") // ArticleComment 를 생성일시 내림차순으로 정렬
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // orphanRemoval = false 상태
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>(); // 중복 불허 (equals(), hashCode()로 판단), 순서 보장


    protected Article() {}

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // 외부에서 아래 정적 팩토리 메소드(of)로 객체 생성 유도
    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
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
