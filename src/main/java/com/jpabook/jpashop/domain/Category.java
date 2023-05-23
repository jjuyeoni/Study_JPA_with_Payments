package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",  // join table name
            joinColumns = @JoinColumn(name = "category_id"), // category_item 테이블에 있는 category id
            inverseJoinColumns = @JoinColumn(name = "item_id")) // category_item 테이블에서 item 의 정보
    private List<Item> items = new ArrayList<>();


    // 계층형 카테고리 구조란 ? 식품 -> 육류 -> 돼지, 소, 닭 등... 이런 구조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // 계층형 카테고리 구조의 부모
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 연관관계 메소드
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
