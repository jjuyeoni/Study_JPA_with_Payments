package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 당시 가격
    private int count; //주문 수량

//    protected OrderItem(){
//    } = @NoArgsConstructor(access = AccessLevel.PROTECTED

    // [생성 메소드]
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // [business logic]
    public void cancel() {
        getItem().addStock(count); // 취소건만큼 재고수량 원복
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
