package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.exception.NotEnoghStockException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속에 대한 전략
@DiscriminatorColumn(name="dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // [business logic] => Setter를 이용하여 직접 값을 변경하지 말고, 함수를 이용하여 이 안에서 값을 변경하는 것이 진정한 객체지향적인 코드.

    //재고 수량 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    //재고 수량 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoghStockException("need more stock");

        }
        this.stockQuantity = restStock;
    }

}
