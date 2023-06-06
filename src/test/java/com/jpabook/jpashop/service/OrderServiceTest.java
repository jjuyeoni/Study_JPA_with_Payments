package com.jpabook.jpashop.service;

import com.jpabook.jpashop.Repository.OrderRepository;
import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.exception.NotEnoghStockException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();

        Book book = createBook("이직하고 싶다", "차주연", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOrder(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 Order");
        assertEquals(1, getOrder.getOrderItems().size(),"주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000*orderCount, getOrder.getTotalPrice(),"주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(),"주문 수량만큼 재고가 줄어야 한다.");
    }



    @Test
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item book = createBook("이직하고 싶다", "차주연", 10000, 10);

        int orderCount = 11;

        //when
        //then
        //첫번째는 exception class, 두번째는 Executable class , 세번째는 String message - junit5
        NotEnoghStockException e = assertThrows(NotEnoghStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        },"재고 수량 부족 예외가 발생해야 한다.");

    }


    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("이직하고 싶다", "차주연", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOrder(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(),"주문 취소시 상태는 Cancel 이다.");
        assertEquals(10, book.getStockQuantity(),"주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");

    }

    private Member createMember() {
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울", "중랑구", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, String author, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

}
