package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MemberRepositoryTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 100);

    @Autowired
    private MemberRepository memberRepository;


    @DisplayName("멤버를 생성한다.")
    @Test
    void saveMember(){
        //given
        Member member = new Member("새로운 멤버","a@a.com", "iconurl");

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getNickname()).isEqualTo(member.getNickname());
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(savedMember.getThumbnail()).isEqualTo(member.getThumbnail());

    }

    @DisplayName("닉네임으로 유저를 검색한다.")
    @Test
    void searchByNickname(){
        memberRepository.save(new Member("imartist","a@a.com","d.url"));
        memberRepository.save(new Member("imartist22","a@a.com","d.url"));
        memberRepository.save(new Member("superhotfire","a@a.com","d.url"));


        String searchName1 = "imartist";
        String searchName2 = "superhotfire";

        Slice<Member> foundMembers = memberRepository.findByNickname_valueContaining(searchName1,PAGEABLE);
        Slice<Member> foundMembers2 = memberRepository.findByNickname_valueContaining(searchName2,PAGEABLE);

        assertThat(foundMembers).hasSize(2)
                .extracting(Member::getNickname)
                .contains(searchName1);
        assertThat(foundMembers2).hasSize(1)
                .extracting(Member::getNickname)
                .contains(searchName2);
    }

    @DisplayName("특정 이메일을 가진 Member를 조회한다.")
    @Test
    void findMemberByEmail(){
        String expectedEmail = "d@d.com";
        Member member1 = memberRepository.save(new Member("n1",expectedEmail));
        Member foundMember = memberRepository.findByEmail(member1.getEmail());

        assertThat(expectedEmail).isEqualTo(foundMember.getEmail());
    }

}
