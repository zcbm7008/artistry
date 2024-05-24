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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="portfolio_contents", joinColumns = @JoinColumn(name = "portfolio_id"))
    private List<PortfolioContent> contents = new ArrayList<>();

    public void addContents(PortfolioContent portfolioContent){
        this.contents.add(portfolioContent);
    }


    public Portfolio(String title,Role role){
        this(null,title,role,null);
    }

    public Portfolio(Long id, @NonNull String title, @NonNull Role role,List<PortfolioContent> contents) {
        this.id = id;
        this.title = title;
        this.role = role;
        this.contents = (contents != null) ? contents : new ArrayList<>();
    }

}
