package com.artistry.artistry;

import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Repository.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final TeamRepository groupRepository;

    public DataLoader(TeamRepository groupRepository){
        this.groupRepository = groupRepository;
    }

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


    }
}
