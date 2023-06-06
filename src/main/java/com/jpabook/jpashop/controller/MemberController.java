package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        // @Valid 을 쓰면 @NotEmpty validation 걸어둔 것을 사용 가능 (또 다른 validation 들도 사용 가능)

        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        if (!form.getPassword1().equals(form.getPassword2())) {
            result.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword1());
        member.setAddress(address);
        member.setRole("ROLE_USER");
        try{
            memberService.join(member);
        }catch (IllegalStateException e){
            e.printStackTrace();
            result.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "members/createMemberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "members/loginForm";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }

}
