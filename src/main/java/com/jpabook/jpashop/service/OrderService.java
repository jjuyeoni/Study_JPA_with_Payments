package com.jpabook.jpashop.service;


import com.jpabook.jpashop.Repository.ItemRepository;
import com.jpabook.jpashop.Repository.MemberRepository;
import com.jpabook.jpashop.Repository.OrderRepository;
import com.jpabook.jpashop.domain.Delivery;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 주문 받을때 배송지 변경 염두하여 추후 수정 예정

        // 주문상품 생성 -> 주문 상품은 하나만 선택할 수 있도록 제약(추후 개선)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); //cascade 옵션 덕분에 item/delivery 에 대한 persist 생략가능
        /* cascade 는 라이프 사이클에 대해 동일하게 관리를 할때 & 다른것이 참조할 수 없는 private owner 인 경우에만 사용 */

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOrder(orderId);
        // 주문 취소
        order.cancel();
    }

    // 주문 검색
//    public List<Order> findOrders(OrderSearch orderSearch){
//
//    }
}
