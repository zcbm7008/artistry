package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Exceptions.ArtistryLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class MemberBioTest {

    private static final String VALID_BIO = "hi i make something";
    private static final String OVER_BIO = "a".repeat(1001);
    private static final String SHORT_BIO = "short bio";
    private static final String MAX_LENGTH_BIO = "a".repeat(1000);


    @DisplayName("bio의 길이가 1000자를 넘어가면 예외를 던진다.")
    @Test
    void lengthTest(){
        assertThatThrownBy(() -> new MemberBio(OVER_BIO)).isInstanceOf(ArtistryLengthException.class);
        assertThatNoException().isThrownBy(() -> new MemberBio(VALID_BIO));
        assertThatNoException().isThrownBy(() -> new MemberBio(SHORT_BIO));
        assertThatNoException().isThrownBy(() -> new MemberBio(MAX_LENGTH_BIO));
    }
}
