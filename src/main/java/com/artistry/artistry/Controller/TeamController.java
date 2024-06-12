package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.ApplicationRequest;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Request.TeamUpdateRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping(value = "/api/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

   @PostMapping
   public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest teamRequest){
        TeamResponse teamResponse = teamService.create(teamRequest);
        return ResponseEntity.created(URI.create("api/teams/" + teamResponse.getId())).body(teamResponse);
   }

   @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> readTeam(@PathVariable Long teamId){
        TeamResponse teamResponse = teamService.findById(teamId);
        return ResponseEntity.ok(teamResponse);
   }

//   @PutMapping("/{teamId}/applications")
//   public ResponseEntity<TeamResponse> applyToTeam(
//           @PathVariable Long teamId,
//           @RequestBody ApplicationRequest request ){
//
//   }

   @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(
           @PathVariable Long teamId,
           @RequestBody @Valid TeamUpdateRequest request
           ) {
        return ResponseEntity.ok(teamService.update(teamId,request));
   }

    @DeleteMapping("/{teamId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long teamId){
        teamService.cancel(teamId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{teamId}/finish")
    public ResponseEntity<TeamResponse> finish(@PathVariable final Long teamId){
        TeamResponse response= teamService.finish(teamId);
        return ResponseEntity.ok(response);
    }

}
