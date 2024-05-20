package com.artistry.artistry.Domain;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nickname;

    @ManyToMany
    @JoinTable(name = "team_member",
            joinColumns = @JoinColumn(name="member_id"),
            inverseJoinColumns = @JoinColumn(name="team_id"))
    private List<Team> teams;

    @OneToMany(mappedBy = "member")
    private List<Portfolio> portfolios = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    public void addPortfolio(Portfolio portfolio){
        this.portfolios.add(portfolio);
        portfolio.setMember(this);
    }


}
