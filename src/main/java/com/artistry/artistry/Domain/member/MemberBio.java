package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Exceptions.ArtistryLengthException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class MemberBio {

    private static final int MAXIMUM_BIO_LENGTH = 1000;

    @Column(length = MAXIMUM_BIO_LENGTH)
    private String bio;

    public MemberBio(String bio){
        validateBioLength(bio);
        this.bio = bio;
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
