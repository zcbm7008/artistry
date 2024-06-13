package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByNickName(String nickName);
    Member findByEmail(String email);

    boolean existsByEmail(String email);
}
