package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping(value = "/api/teams")
@RestController
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

   @PostMapping
   public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest teamRequest){
        TeamResponse teamResponse = teamService.create(teamRequest);
        return ResponseEntity.created(URI.create("api/teams/" + teamResponse.getTeamId())).body(teamResponse);
   }

   @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> readTeam(@PathVariable Long teamId){
        TeamResponse teamResponse = teamService.findById(teamId);
        return ResponseEntity.ok(teamResponse);
   }


}
