package com.artistry.artistry.Domain.common;

import com.artistry.artistry.Exceptions.ArtistryLengthException;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractLink {

    @Transient
    private int MIN_LENGTH;

    @Transient
    private int MAX_LENGTH;

    private String url;
    private String comment;

    @Enumerated(EnumType.STRING)
    private ContentsType type;

    protected AbstractLink(int minLength, int maxLength) {
        this.MIN_LENGTH = minLength;
        this.MAX_LENGTH = maxLength;
    }

    protected AbstractLink(String url, String comment, ContentsType type, int minLength, int maxLength) {
        this(minLength, maxLength);
        validateCommentLength(comment);
        this.url = url;
        this.comment = comment;
        this.type = type;
    }

    public void validateCommentLength(final String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new ArtistryLengthException(
                    String.format("value의 길이는 %d자 이상 %d자 이하여야합니다.", MIN_LENGTH, MAX_LENGTH)
            );
        }
    }
}

