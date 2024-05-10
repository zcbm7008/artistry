package com.artistry.artistry;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        prepareDummyTags();
        prepareDummyMembers();
        prepareDummyRoles();
        prepareDummyTeams();
    }

    private void prepareDummyTags(){

        tagRepository.save(Tag.builder()
                .name("band")
                .build());

        tagRepository.save(Tag.builder()
                .name("edm")
                .build());
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

    private void prepareDummyRoles(){
        roleRepository.save(Role.builder()
                .roleName("vocal")
                .build());

        roleRepository.save(Role.builder()
                .roleName("composer")
                .build());
    }

    private void prepareDummyTeams(){
        List<Tag> tags = Arrays
                .asList(tagRepository.findById(1L).orElseThrow(TagNotFoundException::new),
                        tagRepository.findById(2L).orElseThrow(TagNotFoundException::new));

        List<Member> members = Arrays
                .asList(memberRepository.findById(1L).orElseThrow(MemberNotFoundException::new),
                        memberRepository.findById(2L).orElseThrow(MemberNotFoundException::new));

        List<Role> roles = Arrays
                .asList(roleRepository.findById(1L).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findById(2L).orElseThrow(RoleNotFoundException::new));


        teamRepository.save(Team.builder()
                .host(memberRepository.findById(1L).orElseThrow(MemberNotFoundException::new))
                .name("team1")
                .tags(tags)
                .members(members)
                .roles(roles)
                .build());

        teamRepository.save(Team.builder()
                .host(memberRepository.findById(3L).orElseThrow(MemberNotFoundException::new))
                .name("team2")
                .tags(tags)
                .members(members)
                .roles(roles)
                .build());
    }
}
