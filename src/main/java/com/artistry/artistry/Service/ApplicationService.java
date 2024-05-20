package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Application;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Dto.Response.ApplicationRequestDto;
import com.artistry.artistry.Dto.Response.ApplicationResponseDto;
import com.artistry.artistry.Dto.Response.TeamRequestDto;
import com.artistry.artistry.Dto.Response.TeamResponseDto;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberService memberService;



    public TeamResponseDto findById(Long id){
        return TeamResponseDto.from(teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new));
    }

}

