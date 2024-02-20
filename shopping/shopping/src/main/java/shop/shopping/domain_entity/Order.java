package shop.shopping.domain_entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.shopping.domain_entity.constant.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태[ORDER, CANCEL]

    public static Order createOrder(Member member, LocalDateTime localDateTime, OrderStatus orderStatus) {
        Order order = new Order();
        order.setMember(member);
        order.setOrderDate(localDateTime);
        order.setOrderStatus(orderStatus);
        return order;
    }


}
