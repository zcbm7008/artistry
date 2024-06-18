package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Request.PortfolioRequest;
import com.artistry.artistry.Dto.Request.TeamCreateRequest;
import com.artistry.artistry.Dto.Request.TeamUpdateRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
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
   public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamCreateRequest teamCreateRequest){
        TeamResponse teamResponse = teamService.create(teamCreateRequest);
        return ResponseEntity.created(URI.create("api/teams/" + teamResponse.getId())).body(teamResponse);
   }

   @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> readTeam(@PathVariable Long teamId){
        TeamResponse teamResponse = teamService.findById(teamId);
        return ResponseEntity.ok(teamResponse);
   }

   @PutMapping("/{teamId}/applications")
   public ResponseEntity<ApplicationResponse> applyToTeam(
           @PathVariable Long teamId,
           @RequestBody PortfolioRequest request ){
        return ResponseEntity.ok(teamService.apply(teamId,request));
   }

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
