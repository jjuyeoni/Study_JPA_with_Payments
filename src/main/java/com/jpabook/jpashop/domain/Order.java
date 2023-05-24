package com.jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // name 지정해주지 않으면 관례로 order가 name이 되어버림
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // foreign key
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // 1:1 일때는 fk를 어디에 둬도 상관 없지만 주로 access를 많이 하는 곳으로..
    private Delivery delivery;

//    private Date date; Date 객체 쓰면 날짜관련 어노테이션을 작성해야함.
    private LocalDateTime orderDate; // 자동으로 시간 제공.

    private OrderStatus status; // 주문상태 [Order, Cancel]

    // [연관관계 메소드]
    // 양방향 연관관계 세팅할때 멤버가 주문하면 주문 데이터에서도 넣어줘야함 (양쪽에 값을 세팅)
    public void setMember(Member member){
        this.member = member;
        member.getOrder().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // [생성 메소드]
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem:orderItems){
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.Order);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // [business logic]
    // 주문 취소
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.Cancel); // 주문취소 상태로 변경
        for(OrderItem orderItem:orderItems){
            orderItem.cancel();
        }
    }

    // [조회 로직]
    // 전체 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
