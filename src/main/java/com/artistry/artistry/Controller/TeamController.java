package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Response.TeamRequestDto;
import com.artistry.artistry.Dto.Response.TeamResponseDto;
import com.artistry.artistry.Service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping(value = "/api/teams")
@RestController
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

   @PostMapping
   public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamRequestDto teamRequestDto){
        TeamResponseDto teamResponseDto = teamService.create(teamRequestDto);
        return ResponseEntity.created(URI.create("api/teams/" + teamResponseDto.getTeamId())).body(teamResponseDto);
   }
}
