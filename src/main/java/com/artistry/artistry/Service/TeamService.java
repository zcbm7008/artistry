package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Dto.Response.TeamRequestDto;
import com.artistry.artistry.Dto.Response.TeamResponseDto;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TagService tagService;
    private final RoleService roleService;
    private final MemberService memberService;

    public TeamService(TeamRepository teamRepository,TagService tagService,
                       RoleService roleService, MemberService memberService){
        this.teamRepository = teamRepository;
        this.tagService = tagService;
        this.roleService = roleService;
        this.memberService = memberService;
    }

    public TeamResponseDto findById(Long id){
        return TeamResponseDto.from(teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new));
    }

    public TeamResponseDto create(TeamRequestDto teamRequestDto){
        Team team = Team.builder()
                .host(memberService.findById(teamRequestDto.getHostId()))
                .roles(teamRequestDto.getRoles())
                .tags(teamRequestDto.getTags())
                .build();
        return TeamResponseDto.from(teamRepository.save(team));
    }
}
