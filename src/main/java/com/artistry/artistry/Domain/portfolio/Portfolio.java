package com.artistry.artistry.Domain.portfolio;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Portfolio {

    public static final PortfolioAccess INIT_ACCESS = PortfolioAccess.PUBLIC;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="portfolio_contents", joinColumns = @JoinColumn(name = "portfolio_id"))
    private List<Content> contents = new ArrayList<>();

    @ManyToOne
    private Member member;

    @ManyToOne
    private Role role;

    @Column(name = "view_count")
    private Long view = 0L;
    @Column(name = "like_count")
    private Long like = 0L;

    @Enumerated(EnumType.STRING)
    private PortfolioAccess access = INIT_ACCESS;

    public Portfolio(Member member,String title,Role role){
        this(null,member,title,role,null,INIT_ACCESS);
    }

    public Portfolio(Long id, @NonNull Member member, @NonNull String title, Role role, List<Content> contents, PortfolioAccess access) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.role = role;
        this.contents = (contents != null) ? contents : new ArrayList<>();
        this.access = access;
    }

    public void update(final String title, Role role, List<Content> contents, PortfolioAccess portfolioAccess){
        this.title = title;
        this.role = role;
        this.contents = contents;
        this.access = portfolioAccess;
    }

    public void addContents(List<Content> contents){
        this.contents.addAll(contents);
    }

    public void removeContents(Content content){
        this.contents.remove(content);
    }

    public void addView() { view+=1; }

    public void addLike() { like+=1; }

}
