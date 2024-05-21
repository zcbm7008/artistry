package com.artistry.artistry;

import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public void run(String... args) throws Exception {
        prepareDummyTags();
        prepareDummyMembers();
        prepareDummyRoles();
        prepareDummyPortfolios();
    }


    private void prepareDummyTags(){
        tagRepository.save(new Tag("band"));
        tagRepository.save(new Tag("edm"));
    }

    private void prepareDummyMembers(){
        memberRepository.save(Member.builder()
                .nickname("member1")
                .build());

        memberRepository.save(Member.builder()
                .nickname("member2")
                .build());
        memberRepository.save(Member.builder()
                .nickname("member3")
                .build());
    }

    private void prepareDummyPortfolios(){

        portfolioRepository.save(Portfolio.builder()
                .title("Portfolio1")
                .role(roleRepository.findById(1L).orElseThrow(RoleNotFoundException::new))
                .build());

        portfolioRepository.save(Portfolio.builder()
                .title("Portfolio2")
                .role(roleRepository.findById(2L).orElseThrow(RoleNotFoundException::new))
                .build());
    }

    private void prepareDummyRoles(){
        roleRepository.save(Role.builder()
                .roleName("vocal")
                .build());

        roleRepository.save(Role.builder()
                .roleName("composer")
                .build());
    }
}
