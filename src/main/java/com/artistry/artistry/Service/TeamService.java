package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }

    public Team findById(Long id){
        return teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);
    }
}
