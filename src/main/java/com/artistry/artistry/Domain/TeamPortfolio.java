package com.artistry.artistry.Domain;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TeamPortfolio {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

}
