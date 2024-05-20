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

    @NonNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Portfolio(String title, Role role, Member member){
        this(null,title,role,member);
    }


    public Portfolio(Long id, String title, Role role, Member member) {
        this.id = id;
        this.title = title;
        this.role = role;
        this.member = member;
    }
}
