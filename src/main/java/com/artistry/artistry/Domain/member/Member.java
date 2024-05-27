package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Exceptions.ArtistryInvalidValueException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.security.InvalidParameterException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Nickname nickname;

    @OneToMany(mappedBy = "host")
    @JsonIgnore
    private List<Team> teams;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    private String email;

    private String iconUrl;

    public Member(String nickName){
        this(null,nickName,null,null,null,null);
    }
    public Member (String nickName,String email) {
        this(null,nickName,null,null,email,null);
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public Member(final Long id, final String nickName, final List<Team> teams, List<Application> applications, String email,String iconUrl) {
        validateEmail(email);
        this.id = id;
        this.nickname = new Nickname(nickName);
        this.teams = teams;
        this.applications = applications;
        this.email = email;
        this.iconUrl = iconUrl;
    }

    private void validateEmail(final String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()){
            throw new ArtistryInvalidValueException("이메일 형식이 올바르지 않습니다.");
        }

    }

}
