package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.application.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


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
    @JsonIgnore
    private List<Team> teams;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    public Member(String nickName){
        this(null,nickName,null,null);
    }

    public Member(final Long id, final String nickName, final List<Team> teams, List<Application> applications) {
        this.id = id;
        this.nickname = nickName;
        this.teams = teams;
        this.applications = applications;
    }

}
