package com.artistry.artistry.Domain.common;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public abstract class AbstractLink {
    private String url;
    private String comment;

    @Enumerated(EnumType.STRING)
    private ContentsType type;

    private LengthValidator lengthValidator;

    protected AbstractLink(int minLength, int maxLength) {
        this.lengthValidator = new LengthValidator(minLength, maxLength);
    }

    protected AbstractLink(String url, String comment, ContentsType type, int minLength, int maxLength) {
        this(minLength, maxLength);
        validateCommentLength(comment);
        this.url = url;
        this.comment = comment;
        this.type = type;
    }

    public void validateCommentLength(String comment){
        lengthValidator.validateLength(comment);
    }
}
