package com.artistry.artistry.Domain.Role;

import com.artistry.artistry.Domain.tag.TagName;
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
    private RoleName name;

    public Role(final String roleName) {
        this(null,roleName);
    }

    public Role(final Long id, final String name) {
        this.id = id;
        this.name = new RoleName(name);
    }

    public void update(String name){
        this.name = new RoleName(name);
    }

    public String getName(){
        return name.getValue();
    }

}
