package com.artistry.artistry.Service;

import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamRoleResponse;
import com.artistry.artistry.Exceptions.TeamRoleNotFoundException;
import com.artistry.artistry.Repository.TeamRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamRoleService {

    private final TeamRoleRepository teamRoleRepository;

    public TeamRoleService(TeamRoleRepository teamRoleRepository){
        this.teamRoleRepository = teamRoleRepository;
    }

    public TeamRoleResponse findById(Long id){
        return TeamRoleResponse.from(teamRoleRepository.findById(id)
                .orElseThrow(TeamRoleNotFoundException::new));
    }

    public List<ApplicationResponse> getApplications(Long TeamRoleId){
        TeamRoleResponse teamRoleResponse = this.findById(TeamRoleId);

        return teamRoleResponse.getApplications();
    }
}
