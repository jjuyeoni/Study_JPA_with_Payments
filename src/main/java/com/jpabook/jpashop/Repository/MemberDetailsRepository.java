package com.jpabook.jpashop.Repository;

import com.jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDetailsRepository extends JpaRepository<Member, Integer> {
    Member findByUsername(String name);
}
