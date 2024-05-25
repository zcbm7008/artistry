package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team_role")
@Entity
public class TeamRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "teamRole", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Application> applications = new ArrayList<>();

    public List<Portfolio> getPortfolios(ApplicationStatus status) {
        return applications.stream()
                .filter(application -> status == null || application.getStatus() == status)
                .map(Application::getPortfolio)
                .collect(Collectors.toList());
    }

    public void addApplication(Application application){
        application.setStatus(ApplicationStatus.APPROVED);
        this.getApplications().add(application);
    }


    public String getRoleName(){
        return role.getName();
    }

}
