package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("특정 이메일을 가진 Member를 조회한다.")
    @Test
    void findMemberByEmail(){
        String expectedEmail = "d@d.com";
        Member member1 = memberRepository.save(new Member("n1",expectedEmail));
        Member foundMember = memberRepository.findByEmail(member1.getEmail());

        assertThat(expectedEmail).isEqualTo(foundMember.getEmail());
    }

}
