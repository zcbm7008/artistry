package com.artistry.artistry.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @OneToOne
    @JoinColumn(name="member_id")
    private Member host;

    @OneToMany(mappedBy = "team")
    private List<Member> members;

    @NonNull
    @ManyToMany
    @JoinTable(name = "team_role",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles = new ArrayList<>();

    @NonNull
    @ManyToMany
    @JoinTable(name = "team_tag",
            joinColumns = @JoinColumn(name="team_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Team(Long id, @NonNull String name,@NonNull List<Member> members,@NonNull List<Role> roles,@NonNull List<Tag> tags){
        this.id = id;
        this.name = name;
        this.members=members;
        this.roles = roles;
        this.tags=tags;
    }

}
