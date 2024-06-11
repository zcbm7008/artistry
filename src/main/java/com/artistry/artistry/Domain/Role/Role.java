package com.artistry.artistry.Domain.Role;

import com.artistry.artistry.Domain.team.TeamRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private RoleName name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List <TeamRole> teamRoles = new ArrayList<>();

    public Role(final String roleName) {
        this(null,roleName,null);
    }

    public Role(final Long id, final String name, List<TeamRole> teamRoles) {
        this.id = id;
        this.name = new RoleName(name);
        this.teamRoles = teamRoles;
    }

    public void update(String name){
        this.name = new RoleName(name);
    }

    public String getName(){
        return name.getValue();
    }

}
