package shop.shopping.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.shopping.domain_entity.Member;
import shop.shopping.domain_entity.Order;
import shop.shopping.domain_entity.OrderItem;
import shop.shopping.repository.OrderRepository.OrderItemRepository;
import shop.shopping.repository.OrderRepository.OrderRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public void joinOrder(Order order) {
        orderRepository.save(order);
    }

    public void joinOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }


    public List<OrderItem> findById(Member member) {
        Order order = orderRepository.findOrderId(member).get();
        return orderItemRepository.find(order);
    }



}
