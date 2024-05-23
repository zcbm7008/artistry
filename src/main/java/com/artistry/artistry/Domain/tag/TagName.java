package com.artistry.artistry.Domain.tag;

import com.artistry.artistry.Exceptions.ArtistryLengthException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TagName {
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 20;

    @Column(name = "name")
    @NonNull
    private String value;

    public TagName(final String name){
        validateValue(name);
        this.value = name;
    }
    private static void validateValue(final String name){
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH){
            throw  new ArtistryLengthException(
                    String.format("이름의 길이는 %d자 이상 %d자 이하여야합니다.", MIN_NAME_LENGTH, MAX_NAME_LENGTH)
            );
        }
    }
}
