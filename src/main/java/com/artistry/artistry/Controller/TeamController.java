package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Service.TeamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping("/search")
    public ResponseEntity<List<TeamResponse>> findTeamsByCriteria(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<Long> roleIds,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(defaultValue = "RECRUITING") String status
            , final Pageable pageable) {

        TeamSearchRequest request = new TeamSearchRequest(title,roleIds,tagIds,TeamStatus.of(status));
        return ResponseEntity.ok(teamService.searchTeams(request,pageable));
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
