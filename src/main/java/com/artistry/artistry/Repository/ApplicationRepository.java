package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
}
