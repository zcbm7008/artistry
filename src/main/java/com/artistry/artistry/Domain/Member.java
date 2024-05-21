package com.artistry.artistry.Domain;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nickname;

    @OneToMany(mappedBy = "host")
    private List<Team> teams;

    @OneToMany(mappedBy = "member")
    private List<Portfolio> portfolios = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    public Member(String nickName){
        this(null,nickName,null,null,null);
    }

    public Member(final Long id, final String nickName, final List<Team> teams, List<Portfolio> portfolios, List<Application> applications) {
        this.id = id;
        this.nickname = nickName;
        this.teams = teams;
        this.portfolios = portfolios;
        this.applications = applications;
    }

    public void addPortfolio(Portfolio portfolio){
        this.portfolios.add(portfolio);
        portfolio.setMember(this);
    }


}
