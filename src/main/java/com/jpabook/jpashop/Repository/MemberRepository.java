package com.jpabook.jpashop.Repository;

import com.jpabook.jpashop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext // spring 이 entity manager 주입해줌
    private final EntityManager em;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // managerFactory 직접 주입 받고 싶은 경우
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    public void save(Member member){
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)  // jpql, 반환타입
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.username = :name", Member.class)
                .setParameter("name", name) // jpql에 name 값 set
                .getResultList();
    }

}
