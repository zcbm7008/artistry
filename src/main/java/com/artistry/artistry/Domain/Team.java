package com.artistry.artistry.Domain;

import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
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

    @ManyToMany
    @JoinTable(name = "team_member",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="member_id"))
    private List<Member> members;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "team_role",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

    @ManyToMany
    @JoinTable(name = "team_tag",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private List<Tag> tags;

    @ManyToMany
    @JoinTable(name = "team_portfolio",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="portfolio_id"))
    private List<Portfolio> portfolioList = new ArrayList<>();
    
    @Transient
    private RoleMember applicantMap = new RoleMember();

    @Builder
    public Team(Long id, @NonNull Member host, @NonNull String name, List<Member> members, List<Role> roles, List<Tag> tags){
        this.id = id;
        this.host = host;
        this.name = name;
        this.members = (members != null) ? members : new ArrayList<>();
        this.roles = roles;
        this.tags=tags;
    }

    public void apply(Portfolio portfolio){
        if(isPortfolioInRole(portfolio)){
            throw new ArtistryDuplicatedException("이미 해당 포지션에 지원한 포트폴리오가 있습니다.");
        }
        addPortfolio(portfolio);
    }

    public void addPortfolio(Portfolio portfolio){
        this.portfolioList.add(portfolio);
        applicantMap.add(portfolio.getRole(),portfolio.getMember());
    }

    public boolean isPortfolioInRole(Portfolio portfolio){
        return applicantMap.contains(portfolio.getRole(),portfolio.getMember());
    }

    public void participate(Member member){
        if(isMemberInTeam(member)){
            throw new ArtistryDuplicatedException("이미 해당 팀에 멤버가 있습니다.");
        }

        members.add(member);
    }

    public void acceptMember(Portfolio portfolio){
        portfolioList.remove(portfolio);
        participate(portfolio.getMember());
    }

    public void denyMember(Portfolio portfolio){
        portfolioList.remove(portfolio);
    }

    public boolean isMemberInTeam(Member member){
        return host.equals(member) || members.contains(member);
    }

}
