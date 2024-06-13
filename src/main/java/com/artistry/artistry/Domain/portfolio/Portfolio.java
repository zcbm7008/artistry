package com.artistry.artistry.Domain.portfolio;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Portfolio {

    public static final PortfolioAccess INIT_ACCESS = PortfolioAccess.PUBLIC;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @NonNull
    private String title;

    @ManyToOne
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="portfolio_contents", joinColumns = @JoinColumn(name = "portfolio_id"))
    private List<Content> contents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PortfolioAccess portfolioAccess = INIT_ACCESS;

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


    public Portfolio(Member member,String title,Role role){
        this(null,member,title,role,null,INIT_ACCESS);
    }

    public Portfolio(Long id, @NonNull Member member, @NonNull String title, Role role,List<Content> contents,PortfolioAccess portfolioAccess) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.role = role;
        this.contents = (contents != null) ? contents : new ArrayList<>();
        this.portfolioAccess = portfolioAccess;
    }

}
