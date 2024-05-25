package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
