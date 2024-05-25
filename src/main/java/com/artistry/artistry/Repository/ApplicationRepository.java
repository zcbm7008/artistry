package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
}
