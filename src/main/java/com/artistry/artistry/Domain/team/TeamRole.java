package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
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

    public Application applyPortfolio(Portfolio portfolio){
        validatePortfolio(portfolio);

        Application application = createApplication(portfolio);
        applications.add(application);

        return application;
    }

    private Application createApplication(Portfolio portfolio){
        return new Application(this,portfolio);
    }

    private void validatePortfolio(Portfolio portfolio){
        isMemberDuplicatedInRole(portfolio.getRole(), portfolio.getMember());
    }

    private void isMemberDuplicatedInRole(Role role, Member member){
        if (isDuplicated(role,member)) {
            throw new ArtistryDuplicatedException("멤버의 지원서가 이미 지원한 역할에 있습니다.");
        }
    }

    private boolean isDuplicated(Role role, Member member){
        return  applications.stream()
                .anyMatch(application -> application.getMember().equals(member));
    }

    public String getRoleName(){
        return role.getName();
    }

    public boolean isApprovedInTeamRole(){
        return applications.stream()
                .anyMatch(application -> application.getStatus().equals(ApplicationStatus.APPROVED));
    }

    public void filterApprovedApplications(){
        this.applications = applications.stream()
                .filter(application -> application.getStatus().equals(ApplicationStatus.APPROVED)).collect(Collectors.toList());
    }

    public void removeAllApplications(){
        this.applications = new ArrayList<>();
    }

}
