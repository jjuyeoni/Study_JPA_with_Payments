package com.jpabook.jpashop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded // 내장되었다. (embedded, embeddable 둘 중 하나만 써도 되긴 함)
    private Address address;

    @OneToMany(mappedBy = "member") // order table에 있는 member 에 의해 맵핑됨을 의미.
    private List<Order> order = new ArrayList<>();


}
