package com.artistry.artistry.Domain.portfolio;

import com.artistry.artistry.Domain.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioContent> contents = new ArrayList<>();

    public Portfolio(String title, Role role){
        this(null,title,role,null);
    }

    public Portfolio(Long id, @NonNull String title, Role role,List<PortfolioContent> contents) {
        this.id = id;
        this.title = title;
        this.role = role;
        this.contents = contents;
    }

}
