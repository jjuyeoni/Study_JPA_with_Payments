package com.jpabook.jpashop.controller;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.") //필수 값 지정
    private String username;
    private String password1;
    private String password2;
    private String email;
    private String city;
    private String street;
    private String zipcode;
}
