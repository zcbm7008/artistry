package com.artistry.artistry.Domain;

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

    @NonNull
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

    @Builder
    public Team(Long id, @NonNull Member host,@NonNull String name, List<Member> members,@NonNull List<Role> roles, List<Tag> tags){
        this.id = id;
        this.host = host;
        this.name = name;
        this.members=members;
        initMembers();
        this.roles = roles;
        this.tags=tags;
    }

    public void initMembers(){
        if(this.members == null){
            this.members = new ArrayList<>();
            this.members.add(this.host);
        }
    }

}
