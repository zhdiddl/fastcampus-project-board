package com.fastcampus.projectboard.repository;
import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[DB] JPA 연결 테스트")
class JpaRepositoryTest {

    @Nested // 테스트 클래스 내부에 생성한 두 개의 DB 테스트 클래스를 구분
    @DisplayName("인-메모리 DB 테스트")
    class InMemoryDBTest extends DBTest {

        public InMemoryDBTest(
                @Autowired ArticleRepository articleRepository,
                @Autowired ArticleCommentRepository articleCommentRepository
        ) {
            super(articleRepository, articleCommentRepository);
        }

    }

    @Nested
    @DisplayName("실제 DB 테스트")
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB를 사용하도록 설정
    class ActualDBTest extends DBTest {

        public ActualDBTest(
                @Autowired ArticleRepository articleRepository,
                @Autowired ArticleCommentRepository articleCommentRepository
        ) {
            super(articleRepository, articleCommentRepository);
        }

    }


    @Import(JpaConfig.class)
    @DataJpaTest
    private abstract class DBTest {

        private final ArticleRepository articleRepository;
        private final ArticleCommentRepository articleCommentRepository;

        public DBTest(
                ArticleRepository articleRepository,
                ArticleCommentRepository articleCommentRepository
        ) {
            this.articleRepository = articleRepository;
            this.articleCommentRepository = articleCommentRepository;
        }

        @DisplayName("select 테스트")
        @Test
        void givenTestData_whenSelecting_thenWorksFine() {
            // Given

            // When
            List<Article> articles = articleRepository.findAll();

            // Then
            assertThat(articles)
                    .isNotNull()
                    .hasSize(123);
        }

        @DisplayName("insert 테스트")
        @Test
        void givenTestData_whenInserting_thenWorksFine() {
            // Given
            long previousCount = articleRepository.count();

            // When
            articleRepository.save(Article.of("new article", "new content", "#spring"));

            // Then
            assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
        }

        @DisplayName("update 테스트")
        @Test
        void givenTestData_whenUpdating_thenWorksFine() {
            // Given
            Article article = articleRepository.findById(1L).orElseThrow();
            String updatedHashtag = "#springboot";
            article.setHashtag(updatedHashtag);

            // When
            Article savedArticle = articleRepository.saveAndFlush(article);

            // Then
            assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
        }

        @DisplayName("delete 테스트")
        @Test
        void givenTestData_whenDeleting_thenWorksFine() {
            // Given
            Article article = articleRepository.findById(1L).orElseThrow();
            long previousArticleCount = articleRepository.count();
            long previousArticleCommentCount = articleCommentRepository.count();
            int deletedCommentsSize = article.getArticleComments().size();

            // When
            articleRepository.delete(article);

            // Then
            assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
            assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
        }

    }

}