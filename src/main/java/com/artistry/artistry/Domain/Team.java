package com.artistry.artistry.Domain;

import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member host;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamRole> teamRoles = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "team_tag",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private List<Tag> tags;


    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @Builder
    public Team(Long id, @NonNull Member host, @NonNull String name,List<TeamRole> teamRoles,List<Tag> tags){
        this.id = id;
        this.host = host;
        this.name = name;
        this.tags=tags;
        this.teamRoles = new ArrayList<>();
    }

    public void apply(TeamRole teamRole, Application application){
        if(teamRole.getApplications().contains(application)){
            throw new ArtistryDuplicatedException("이미 해당 역할에 지원한 포트폴리오가 있습니다.");
        }
        teamRole.getApplications().add(application);
        application.setTeamRole(teamRole);
    }

    public void addRoles(List<Role> roles){
        this.teamRoles.addAll(
                roles.stream()
                        .map(this::roleToTeamRole)
                        .toList()
        );
    }

    public TeamRole roleToTeamRole(Role role){
        return(TeamRole.builder()
                .team(this)
                .role(role)
                .applications(new ArrayList<>())
                .build());
    }

    public TeamRole findTeamRoleByRole(Role role){
        return teamRoles.stream()
                .filter(teamRole -> teamRole.getRole().equals(role))
                .findFirst()
                .orElseThrow(RoleNotFoundException::new);
    }

    public boolean isHostMember(Member member){
        return host.equals(member);
    }

}