package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Domain.tag.TagName;
import com.artistry.artistry.Exceptions.ArtistryInvalidValueException;
import com.artistry.artistry.Exceptions.ArtistryLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class MemberTest {

    @DisplayName("email이 형식에 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"a1","@gmail.com","asfasf.asfasf","as@asad@asdsad"})
    void validateEmail(final String email){
        //when, then
        assertThatExceptionOfType(ArtistryInvalidValueException.class).isThrownBy(() -> new Member("artist1",email));
    }
}
