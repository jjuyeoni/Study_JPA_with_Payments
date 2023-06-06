package com.jpabook.jpashop.service;
import com.jpabook.jpashop.Repository.MemberDetailsRepository;
import com.jpabook.jpashop.Repository.MemberRepository;
import com.jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailsService implements UserDetailsService {

    private final MemberDetailsRepository memberDetailsRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("name :"+ username);
        Member member = memberDetailsRepository.findByUsername(username);
        if(member != null){
            return new MemberDetails(member);
        }
        return null;
    }

}
