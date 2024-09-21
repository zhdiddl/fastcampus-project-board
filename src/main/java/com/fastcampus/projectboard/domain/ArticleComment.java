package com.fastcampus.projectboard;

import java.time.LocalDateTime;

public class ArticleComment {
    private Long id;
    private Article article;
    private String hashtag;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
