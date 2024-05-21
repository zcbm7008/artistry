package com.artistry.artistry.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<TeamRole> teamRoles = new ArrayList<>();

    public Role(final String roleName) {
        this(null,roleName,null);
    }


}
