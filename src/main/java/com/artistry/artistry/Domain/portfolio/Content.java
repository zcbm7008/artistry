package com.artistry.artistry.Domain.portfolio;

import com.artistry.artistry.Domain.common.AbstractLink;
import com.artistry.artistry.Domain.common.ContentsType;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Content extends AbstractLink {
    private static final int MIN_COMMENT_LENGTH = 1;
    private static final int MAX_COMMENT_LENGTH = 20;

    public Content() {
        super(MIN_COMMENT_LENGTH, MAX_COMMENT_LENGTH);
    }

    public Content(String url, String comment) {
        this(url, comment, ContentsType.WEB);
    }

    public Content(String url, String comment, ContentsType type) {
        super(url, comment, type, MIN_COMMENT_LENGTH, MAX_COMMENT_LENGTH);
    }
}
