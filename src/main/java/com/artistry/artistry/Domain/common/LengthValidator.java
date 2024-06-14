package com.artistry.artistry.Domain.common;

import com.artistry.artistry.Exceptions.ArtistryLengthException;
import org.springframework.stereotype.Component;

@Component
public class LengthValidator {
    private final int MIN_LENGTH;
    private final int MAX_LENGTH;

    public LengthValidator(final int MIN_LENGTH,final int MAX_LENGTH){
        this.MIN_LENGTH = MIN_LENGTH;
        this.MAX_LENGTH = MAX_LENGTH;
    }

    public void validateLength(final String value){
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH){
            throw new ArtistryLengthException(
                    String.format("value의 길이는 %d자 이상 %d자 이하여야합니다.", MIN_LENGTH, MAX_LENGTH)
            );
        }
    }

}
