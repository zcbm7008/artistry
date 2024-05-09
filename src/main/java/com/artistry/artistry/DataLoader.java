package com.artistry.artistry;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TeamRepository groupRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        prepareDummyTeams();
        prepareDummyTags();
        prepareDummyMembers();
        prepareDummyRoles();
    }

    private void prepareDummyTeams(){
        groupRepository.save(Team.builder()
                .id(1L)
                .name("Group1")
                .build());

        groupRepository.save(Team.builder()
                .id(2L)
                .name("Group2")
                .build());

    }

    private void prepareDummyTags(){

        tagRepository.save(Tag.builder()
                .id(1L)
                .name("band")
                .build());

        tagRepository.save(Tag.builder()
                .id(2L)
                .name("edm")
                .build());
    }

    private void prepareDummyMembers(){
        memberRepository.save(Member.builder()
                .id(1L)
                .nickname("member1")
                .build());

        memberRepository.save(Member.builder()
                .id(2L)
                .nickname("member2")
                .build());
    }

    private void prepareDummyRoles(){
        roleRepository.save(Role.builder()
                .id(1L)
                .roleName("vocal")
                .build());

        roleRepository.save(Role.builder()
                .id(2L)
                .roleName("composer")
                .build());
    }
}
