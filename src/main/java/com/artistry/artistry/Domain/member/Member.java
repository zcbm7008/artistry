package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Exceptions.ArtistryInvalidValueException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
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

    @NonNull
    private String email;

    private String iconUrl;

    @Column(nullable = false)
    private boolean deleted;

    public Member(String nickName){
        this(null,nickName,null,null,null,null,false);
    }
    public Member (String nickName,String email) {
        this(null,nickName,email,null,null,null,false);
    }

    public Member(String nickName, String email, String iconUrl) {
        this(null,nickName,email,iconUrl,null,null,false);
    }

    public String getNickname() {
        return nickname.getValue();
    }

    @Builder
    public Member(final Long id, @NonNull final String nickName, @NonNull String email,String iconUrl, final List<Team> teams, List<Application> applications,boolean deleted) {
        validateEmail(email);
        this.id = id;
        this.nickname = new Nickname(nickName);
        this.teams = teams;
        this.applications = applications;
        this.email = email;
        this.iconUrl = iconUrl;
        this.deleted = deleted;
    }

    public void update(String nickName, String iconUrl){
        this.nickname = new Nickname(nickName);
        this.iconUrl = iconUrl;
    }

    private void validateEmail(final String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()){
            throw new ArtistryInvalidValueException("이메일 형식이 올바르지 않습니다.");
        }

    }

}
