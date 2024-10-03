package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>, // 기본적인 CRUD
        QuerydslPredicateExecutor<Article>, // Querydsl 사용해서 동적 쿼리 실행
        QuerydslBinderCustomizer<QArticle> { // Querydsl 사용해서 검색할 필드 설정 후 검색 방법 커스터마이징

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true); // 명시한 필드가 아니면 검색 필드에서 제외하는 것으로 설정
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 검색에 포함할 필드 지정
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // 대소문자 구분 없음, 부분 문자열 검색 가능
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 정확히 일치할 때만 검색
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
