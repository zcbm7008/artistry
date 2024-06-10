package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
import com.artistry.artistry.Exceptions.TeamNotRecruitingException;
import com.artistry.artistry.Exceptions.TeamRoleHasApprovedException;
import com.artistry.artistry.Exceptions.TeamRoleNotFoundException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@SQLDelete(sql = "UPDATE team SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
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
    @JoinColumn(name = "host_id")
    private Member host;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TeamRole> teamRoles = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "team_tag",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private List<Tag> tags;

    private boolean isRecruiting = true;

    @Column(nullable = false)
    private boolean deleted;

    public Team(final String name,final Member host,final List<Tag> tags, List<Role> roles){
        this(null,name,host,tags,roles,true,false);
    }

    public Team(final String name,final Member host, List<Role> roles){
        this(null,name,host,null,roles,true,false);
    }


    @Builder
    public Team(Long id, @NonNull String name, @NonNull Member host, List<Tag> tags, List<Role> roles,boolean isRecruiting,boolean deleted){
        this.id = id;
        this.name = name;
        this.host = host;
        this.tags = tags;
        addRoles(roles);
        this.isRecruiting = isRecruiting;
        this.deleted = deleted;
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

    public void apply(Application application){
        validateApplication(application);
        TeamRole teamRole = findTeamRoleByRole(application.getRole());
        teamRole.getApplications().add(application);
        application.setTeamRole(teamRole);
    }

    private void validateApplication(Application application){
        isTeamRecruiting();
        isMemberDuplicatedInRole(application.getRole(), application.getMember());
        isValidRole(application.getRole());
    }

    private void isTeamRecruiting(){
        if (!isRecruiting){
            throw new TeamNotRecruitingException("팀이 구인중인 상태가 아닙니다.");
        }
    }

    private void isMemberDuplicatedInRole(Role role, Member member){
        if (isDuplicated(role,member)) {
            throw new ArtistryDuplicatedException("멤버의 지원서가 이미 지원한 역할에 있습니다.");
        }
    }

    private boolean isDuplicated(Role role, Member member){
        return findTeamRoleByRole(role).getApplications().stream()
                .anyMatch(application -> application.getMember().equals(member));
    }

    private void isValidRole(Role role){
        if(!isRoleInTeam(role)){
            throw new TeamRoleNotFoundException(String.format("[%s]는 팀의 역할에 없습니다.", role.getName()));
        }
    }

    private boolean isRoleInTeam(Role role){
        return teamRoles.stream()
                .anyMatch(teamRole -> teamRole.getRole().equals(role));
    }

    public TeamRole findTeamRoleByRole(Role role){
        isValidRole(role);
        return teamRoles.stream()
                .filter(teamRole -> teamRole.getRole().equals(role))
                .findFirst()
                .orElseThrow(TeamRoleNotFoundException::new);
    }

    private void validateTeamRoleForDeletion(TeamRole teamRole){
        if(teamRole.isApprovedInTeamRole()){
            throw new TeamRoleHasApprovedException("이 역할에 이미 승인된 멤버가 있습니다.");
        }
    }

    public boolean isHostMember(Member member){
        return host.equals(member);
    }

    public void update(@NonNull String name, List<Tag> tags, List<Role> roles,boolean isRecruiting){
        this.name = name;
        this.tags = tags;
        updateRoles(roles);
        this.isRecruiting = isRecruiting;
    }

    private void updateRoles(List<Role> roles) {
        List<Role> rolesToAdd = findRolesNotInTeamRoles(roles);
        List<TeamRole> teamRolesToDelete = findTeamRolesNotInRoles(roles);

        // 새 역할 추가 로직
        rolesToAdd.forEach(role -> this.teamRoles.add(roleToTeamRole(role)));

        // 기존 역할 삭제 로직
        teamRolesToDelete.forEach(this::deleteTeamRoleByRole);
    }

    private List<Role> findRolesNotInTeamRoles(List<Role> roles) {
        List<String> existingRoleNames = this.teamRoles.stream()
                .map(teamRole -> teamRole.getRole().getName())
                .toList();

        return roles.stream()
                .filter(role -> !existingRoleNames.contains(role.getName()))
                .collect(Collectors.toList());
    }

    private List<TeamRole> findTeamRolesNotInRoles(List<Role> roles) {
        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .toList();

        return this.teamRoles.stream()
                .filter(teamRole -> !roleNames.contains(teamRole.getRole().getName()))
                .collect(Collectors.toList());
    }

    private void deleteTeamRoleByRole(TeamRole teamRole){
        validateTeamRoleForDeletion(teamRole);
        this.teamRoles.remove(teamRole);
    }

}