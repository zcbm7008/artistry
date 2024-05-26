package com.artistry.artistry.Domain.portfolio;

import com.artistry.artistry.Domain.Role.Role;
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
    private List<Content> contents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PortfolioAccess portfolioAccess = PortfolioAccess.PRIVATE;

    public void addContents(List<Content> contents){
        this.contents.addAll(contents);
    }

    public void removeContents(Content content){
            this.contents.remove(content);
    }


    public void update(final String title, Role role, List<Content> contents,PortfolioAccess portfolioAccess){
        this.title = title;
        this.role = role;
        this.contents = contents;
        this.portfolioAccess = portfolioAccess;
    }


    public Portfolio(String title,Role role){
        this(null,title,role,null,PortfolioAccess.PRIVATE);
    }

    public Portfolio(Long id, @NonNull String title, Role role,List<Content> contents,PortfolioAccess portfolioAccess) {
        this.id = id;
        this.title = title;
        this.role = role;
        this.contents = (contents != null) ? contents : new ArrayList<>();
        this.portfolioAccess = portfolioAccess;
    }

}
