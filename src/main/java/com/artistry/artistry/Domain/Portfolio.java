package com.artistry.artistry.Domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @ManyToOne
    private Role role;

    public Portfolio(String title, Role role){
        this(null,title,role);
    }


    public Portfolio(Long id, @NonNull String title, Role role) {
        this.id = id;
        this.title = title;
        this.role = role;
    }
}
