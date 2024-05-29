package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

}
