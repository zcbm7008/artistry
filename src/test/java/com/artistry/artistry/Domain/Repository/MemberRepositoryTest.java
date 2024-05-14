package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("유저 더미 데이터를 확인한다.")
    @ParameterizedTest
    @CsvSource({"1, member1","2, member2"})
    void dummyUserTest(Long id, String nickname){
        Optional<Member> user = memberRepository.findById(id);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getNickname()).isEqualTo(nickname);
    }
}
