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

    public static final ApplicationStatus INIT_STATUS = ApplicationStatus.PENDING;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teamRole_id")
    private TeamRole teamRole;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = INIT_STATUS;

    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType = ApplicationType.APPLICATION;

    public Application(Portfolio portfolio){
        this(null,null,portfolio,INIT_STATUS,ApplicationType.APPLICATION);
    }

    public Application(TeamRole teamRole,Portfolio portfolio){
        this(null,teamRole,portfolio,INIT_STATUS,ApplicationType.APPLICATION);
    }

    public Application(TeamRole teamRole, Portfolio portfolio, ApplicationType applicationType) {
        this(null,teamRole,portfolio,INIT_STATUS,applicationType);
    }

    public Member getDecisionMaker() {
        if (applicationType == ApplicationType.APPLICATION) {
            return this.teamRole.getTeam().getHost();
        } else if (applicationType == ApplicationType.INVITATION) {
            return this.portfolio.getMember();
        }
        throw new IllegalStateException("Unknown application type: " + applicationType);
    }

    public Role getRole(){
        return this.portfolio.getRole();
    }

    public Member getMember(){
        return this.portfolio.getMember();
    }

    public Team getTeam(){
        return this.teamRole.getTeam();
    }

}
