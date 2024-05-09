package com.artistry.artistry;

import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TeamRepository groupRepository;
    private final TagRepository tagRepository;


    @Override
    public void run(String... args) throws Exception {
        groupRepository.save(Team.builder()
                .id(1L)
                .name("Group1")
                .build());

        groupRepository.save(Team.builder()
                .id(2L)
                .name("Group2")
                .build());

        tagRepository.save(Tag.builder()
                .id(1L)
                .name("band")
                .build());

        tagRepository.save(Tag.builder()
                .id(2L)
                .name("edm")
                .build());




    }
}
