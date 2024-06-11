package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
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
    @NonNull
    private Role role;

    @OneToMany(mappedBy = "teamRole", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();

    public List<Portfolio> getAllPortfolios() {
        return applications.stream()
                .map(Application::getPortfolio)
                .collect(Collectors.toList());
    }


    public List<Portfolio> getPortfoliosByStatus(ApplicationStatus status) {
        return applications.stream()
                .filter(application -> status == null || application.getStatus() == status)
                .map(Application::getPortfolio)
                .collect(Collectors.toList());
    }

    public void addApplication(Application application){
        application.setTeamRole(this);
        applications.add(application);
    }

    public String getRoleName(){
        return role.getName();
    }

    public boolean isApprovedInTeamRole(){
        return applications.stream()
                .anyMatch(application -> application.getStatus().equals(ApplicationStatus.APPROVED));
    }

    public void filterApprovedApplications(){
        applications.removeIf(application -> !application.getStatus().equals(ApplicationStatus.APPROVED));

    }

    public void removeAllApplications(){
        applications.clear();
    }

}
