package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter @Column(nullable = false) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter private String email;
    @Setter private String nickname;
    @Setter private String memo;


    protected UserAccount() {} // JPA 가 엔티티 객체 생성시 사용할 수 있는 최소한의 접근 제어자

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;

        if (this.getId() == null) {
            return Objects.equals(this.getUserId(), that.getUserId()) &&
                    Objects.equals(this.getUserPassword(), that.getUserPassword()) &&
                    Objects.equals(this.getEmail(), that.getEmail()) &&
                    Objects.equals(this.getNickname(), that.getNickname()) &&
                    Objects.equals(this.getMemo(), that.getMemo()) &&
                    Objects.equals(this.getCreatedAt(), that.getCreatedAt()) &&
                    Objects.equals(this.getCreatedBy(), that.getCreatedBy()) &&
                    Objects.equals(this.getModifiedAt(), that.getModifiedAt()) &&
                    Objects.equals(this.getModifiedBy(), that.getModifiedBy());
        }

        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        if (this.getId() == null) {
            return Objects.hash(getUserId(), getUserPassword(), getEmail(), getNickname(), getMemo(), getCreatedAt(), getCreatedBy(), getModifiedAt(), getModifiedBy());
        }

        return Objects.hash(getUserId());
    }

}
