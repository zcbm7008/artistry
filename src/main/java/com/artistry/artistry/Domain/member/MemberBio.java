package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Exceptions.ArtistryLengthException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Embeddable
public class MemberBio {

    private static final int MAXIMUM_BIO_LENGTH = 1000;

    @Column(name = "bio", length = MAXIMUM_BIO_LENGTH)
    private String value;

    public MemberBio(String value){
        validateBioLength(value);
        this.value = value;
    }

    protected MemberBio() {

    }

    public void validateBioLength(final String value) {
        if (MAXIMUM_BIO_LENGTH < value.length()) {
            throw new ArtistryLengthException(
                    String.format("bio의 길이는 %d자 이하여야합니다.", MAXIMUM_BIO_LENGTH)
            );
        }
    }

}
