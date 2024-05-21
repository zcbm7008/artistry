package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
