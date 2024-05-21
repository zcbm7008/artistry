package com.artistry.artistry.Domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String title;

    @ManyToOne
    private Role role;

    public Portfolio(String title, Role role){
        this(null,title,role);
    }


    public Portfolio(Long id, String title, Role role) {
        this.id = id;
        this.title = title;
        this.role = role;
    }
}
