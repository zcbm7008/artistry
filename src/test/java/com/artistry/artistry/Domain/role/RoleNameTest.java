package com.artistry.artistry.Domain.role;

import com.artistry.artistry.Domain.tag.TagName;
import com.artistry.artistry.Exceptions.ArtistryLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class RoleNameTest {
    @DisplayName("1자 이상 10자 이하일 경우 생성")
    @ValueSource(strings = {"작곡가","일러스트레이터","영상편집"})
    @ParameterizedTest
    void validTagNameLengthTest(final String name){
        assertThatCode(() -> new TagName(name)).doesNotThrowAnyException();
    }

    @DisplayName("1자보다 짧거나 10자보다 길 경우 예외처리")
    @ValueSource(strings = {"","작곡가가가가가가가가가가가가가가가가가가가가가가가가가가가"})
    @ParameterizedTest
    void InvalidTagNameLengthTest(final String name){
        assertThatExceptionOfType(ArtistryLengthException.class).isThrownBy(() -> new TagName(name));
    }

}
