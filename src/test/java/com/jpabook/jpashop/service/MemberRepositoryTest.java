package com.jpabook.jpashop.service;

import com.jpabook.jpashop.Repository.MemberRepository;
import com.jpabook.jpashop.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(false) // no rollback
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("Cha");

        //when
        Long savedId = memberService.join(member);

        //then
//        em.flush(); // 영속성 context에 있는 내용을 db에 반영 @Rollback 대신 사용
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setUsername("Cha");

        Member member2 = new Member();
        member2.setUsername("Cha");

        //when
        memberService.join(member1);

        //then
        assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class);

//        fail("예외 발생");
    }
}
