package com.artistry.artistry.Domain.portfolio;

import com.artistry.artistry.Domain.common.AbstractLink;
import com.artistry.artistry.Domain.common.ContentsType;
import com.artistry.artistry.Domain.common.LengthValidator;
import com.artistry.artistry.Exceptions.ArtistryLengthException;
import io.swagger.v3.oas.annotations.links.Link;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Content extends AbstractLink {
    private static final int MIN_COMMENT_LENGTH = 1;
    private static final int MAX_COMMENT_LENGTH = 20;

    private final LengthValidator lengthValidator = new LengthValidator(MIN_COMMENT_LENGTH,MAX_COMMENT_LENGTH);
    private String url;
    private String comment;

    public Content(String url, String comment){
        this(url, comment, ContentsType.UNKNOWN);
    }

    public Content(String url, String comment, ContentsType type){
        super(url, comment, type, MIN_COMMENT_LENGTH, MAX_COMMENT_LENGTH);
    }
}
