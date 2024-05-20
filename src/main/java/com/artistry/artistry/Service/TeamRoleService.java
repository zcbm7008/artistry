package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.TeamRole;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Exceptions.TeamRoleNotFoundException;
import com.artistry.artistry.Repository.TeamRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamRoleService {

    private final TeamRoleRepository teamRoleRepository;

    public TeamRoleService(TeamRoleRepository teamRoleRepository){
        this.teamRoleRepository = teamRoleRepository;
    }

    public TeamRole findById(Long id){
        return teamRoleRepository.findById(id)
                .orElseThrow(TeamRoleNotFoundException::new);
    }

    public List<ApplicationResponse> getApplications(Long TeamRoleId){
        TeamRole teamRole = this.findById(TeamRoleId);

        return teamRole.getApplications().stream()
                .map(ApplicationResponse::from)
                .collect(Collectors.toList());
    }
}
