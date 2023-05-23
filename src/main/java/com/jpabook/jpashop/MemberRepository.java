package com.jpabook.jpashop;


import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em; // spring boot 가 manager 자동 생성해줌.

    public Long save(Member member){
        em.persist(member);
        return member.getId(); // command 와 query 분리를 위해 id만 조회.
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
