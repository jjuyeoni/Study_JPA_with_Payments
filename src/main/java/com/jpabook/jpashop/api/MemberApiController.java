package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@Controller + @ResponseBody = @RestController
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> memberV1(){
        return memberService.findMembers();
        /**
         * 잘못된 API
         이렇게 짜면 entity에 있는 정보들이 모두 공개가 됨

         Entity class에
         @JsonIgnore <- 어노테이션을 사용하면 필요없는 정보는 제외되고 출력되지만
         private List<Order> order = new ArrayList<>();

         1. 이렇게 되면 entity에 의존관계가 나가게 되면서 양방향 의존관계가 걸림
         -> application 수정이 어렵게 됨.

         2. 만약 entity의 변수가 변경될 경우, api 스펙이 변경되어 버림.
         ex. name -> username으로 변경될 경우, 응답 스펙이 맞도록 api가 또 변경되어야함.

         3. 이 경우 Array로 넘어오기 때문에 스펙 확장이 불가능함. (유연성이 떨어짐)
         */
    }

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getUsername()))
                .collect(Collectors.toList());

        return new Result(collect.size(),collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setUsername(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
            // create와 구조가 달라지기 때문에 request를 따로 생성

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        // update에서 member을 반환하지 않게 짠 이유는 command와 query를 분리하기 위해서
        // 이건 개발 스타일에 따라 다름 => 위 방법이 유지보수성에 좋음

        return new UpdateMemberResponse(findMember.getId(), findMember.getUsername());
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
