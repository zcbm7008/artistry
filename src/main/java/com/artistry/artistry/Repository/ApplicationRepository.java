package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    List<Application> findByStatusAndPortfolio_Member(ApplicationStatus status, Member member);

    List<Application> findByStatus(ApplicationStatus status);
}
