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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<TeamRole> teamRoles = new ArrayList<>();

    public static Role of(@NonNull String roleName) {
        return Role.builder()
                .roleName(roleName)
                .build();
    }

}
