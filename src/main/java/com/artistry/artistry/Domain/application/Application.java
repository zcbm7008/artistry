package com.artistry.artistry.Domain.application;


import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamRole;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "teamRole_id")
    private TeamRole teamRole;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    public Application(Team team, Role role, Member member, Portfolio portfolio){
        this(null,team,role,null,member,portfolio,ApplicationStatus.PENDING);
    }

    public Application(Team team, Role role, TeamRole teamRole,Member member, Portfolio portfolio){
        this(null,team,role,teamRole,member,portfolio,ApplicationStatus.PENDING);
    }
}
