package com.artistry.artistry.Domain.member;

import com.artistry.artistry.Domain.portfolio.Portfolio;
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

import java.util.ArrayList;
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

    @NonNull
    private String email;

    private ProfileImage thumbnail = new ProfileImage();

    private MemberBio bio;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="memberLinks", joinColumns = @JoinColumn(name = "member_id"))
    private List<MemberLink> memberLinks = new ArrayList<>();

    @OneToMany(mappedBy = "host")
    @JsonIgnore
    private List<Team> hostTeams;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios;

    @Column(nullable = false)
    private boolean deleted;

    public Member (String nickName,String email) {
        this(null,nickName,email,new ProfileImage()," ",new ArrayList<>(),null,null,false);
    }

    public Member(String nickName, String email, String thumbnail) {
        this(null,nickName,email, new ProfileImage(thumbnail)," ",new ArrayList<>(),null,null,false);
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getBio() { return bio.getValue();}

    @Builder
    public Member(final Long id,
                  @NonNull final String nickName,
                  final @NonNull String email,
                  final ProfileImage thumbnail,
                  final String bio,
                  final List<MemberLink> memberLinks,
                  final List<Team> hostTeams,
                  final List<Portfolio> portfolios,
                  final boolean deleted) {
        validateEmail(email);
        this.id = id;
        this.nickname = new Nickname(nickName);
        this.email = email;
        this.thumbnail = thumbnail;
        this.bio = new MemberBio(bio);
        this.memberLinks = memberLinks;
        this.hostTeams = hostTeams;
        this.portfolios = portfolios;
        this.deleted = deleted;
    }

    public void update(String nickName, ProfileImage thumbnail,String bio,List<MemberLink> links){
        this.nickname = new Nickname(nickName);
        this.thumbnail = thumbnail;
        this.bio = new MemberBio(bio);
        if(this.memberLinks == null || this.memberLinks.isEmpty()){
            this.memberLinks = new ArrayList<>();
        }
        this.memberLinks = links;
    }

    private void validateEmail(final String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()){
            throw new ArtistryInvalidValueException("이메일 형식이 올바르지 않습니다.");
        }

    }
}
