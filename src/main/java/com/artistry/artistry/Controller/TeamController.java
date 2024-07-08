package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Service.TeamSearchService;
import com.artistry.artistry.Service.TeamService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping(value = "/api/teams")
@RestController
public class TeamController {

    private final TeamService teamService;
    private final TeamSearchService teamSearchService;

    public TeamController(TeamService teamService, TeamSearchService teamSearchService){
        this.teamService = teamService;
        this.teamSearchService = teamSearchService;
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

   @Cacheable(value = "teamSearchCache", key = "{#name, #roleIds, #tagIds, #status}")
   @GetMapping("/search")
   public ResponseEntity<List<TeamResponse>> findTeamsByCriteria(
           @RequestParam(required = false) String name,
           @RequestParam(required = false) List<Long> roleIds,
           @RequestParam(required = false) List<Long> tagIds,
           @RequestParam(defaultValue = "RECRUITING") TeamStatus status
           , final Pageable pageable) {

        TeamSearchRequest request = new TeamSearchRequest(name,roleIds,tagIds,status);
        return ResponseEntity.ok(teamSearchService.searchTeams(request,pageable));
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
