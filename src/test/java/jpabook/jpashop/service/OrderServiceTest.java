package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.*;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 주문() throws Exception {
//        given
        Member member = createMember();

        Book book = createBook("Leviathan", 10000, 10);

        int orderCount = 2;

//        when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

//        then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("Item's status should be ORDER: ", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.",1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * 2, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",8, book.getStockQuantity());
    }

    @Test
    public void 주문_취소() throws Exception {
//        given
        Member member = createMember();
        Book book = createBook("Leviathan", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

//        when
        orderService.cancelOrder(orderId);

//        then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("Item's status should be CANCEL: ", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("재고수량 원복: ", 10, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고수량_초과_검증() throws Exception {
//        given
        Member member = createMember();
        Item item = createBook("Leviathan", 10000, 10);

        int orderCount = 2;

//        when
        orderService.order(member.getId(), item.getId(), orderCount);

//        then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("hazel");
        member.setAddress(new Address("Seoul", "wall_st", "12345"));
        em.persist(member);
        return member;
    }

}