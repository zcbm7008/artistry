package com.artistry.artistry;

import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Transactional
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
        tagRepository.save(new Tag("밴드"));
        tagRepository.save(new Tag("재즈"));
    }

    private void prepareDummyMembers(){
        memberRepository.save(new Member("composer1"));
        memberRepository.save(new Member("art1"));
        memberRepository.save(new Member("movieman1"));

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
        roleRepository.save(new Role("작곡가"));
        roleRepository.save(new Role("일러스트레이터"));
    }
}
