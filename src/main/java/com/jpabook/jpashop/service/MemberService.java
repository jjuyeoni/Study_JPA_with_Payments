package com.jpabook.jpashop.service;

import com.jpabook.jpashop.Repository.MemberRepository;
import com.jpabook.jpashop.domain.Member;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; //spring이 제공하는 걸 쓰는걸 권장.

import java.util.List;

//@AllArgsConstructor // 생성자를 자동으로 만들어줌
@Service
@Transactional(readOnly = true) // 데이터 변경할 때에는 꼭 트랜젝션을 이욯해야함.  // jpa가 읽기 전용 트랜잭션인 것을 알고 리소스 부하를 막아줌
@RequiredArgsConstructor // 파이널이 있는 필드만 가지고 생성자 만들어줌
public class MemberService {
    
    private final MemberRepository memberRepository;

//    @Autowired // for injection, 생성자가 하나만 있는 경우 스프링이 자동으로 injection 해줘서 안써도 됨
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    @Transactional
    // 회원 가입
    public Long join(Member member){
        
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    // 회원 전체 조회
    public List<Member> findMemberS(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
    
}
