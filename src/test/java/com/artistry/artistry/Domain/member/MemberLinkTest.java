package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Exceptions.ArtistryLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

public class MemberLinkTest {
    @DisplayName("Link가 형식에 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @CsvSource({
            "a.url,short",       // 유효한 URL과 유효한 코멘트
            "a.url,toolongcommentexceedsmax", // 유효한 URL과 너무 긴 코멘트
            "another.url,valid", // 유효한 URL과 유효한 코멘트
            "valid.url,thisislong11" // 유효한 URL과 너무 긴 코멘트
    })
    void validateLink(String url, String comment) {
        if (comment.length() > 10 || comment.length() < 1) {
            assertThatExceptionOfType(ArtistryLengthException.class).isThrownBy(() -> new MemberLink(url, comment));
        } else {
            assertThatNoException().isThrownBy(() -> new MemberLink(url, comment));
        }
    }
}
