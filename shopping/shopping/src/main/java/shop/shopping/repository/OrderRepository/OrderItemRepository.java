package shop.shopping.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.Order;
import shop.shopping.domain_entity.OrderItem;

import java.util.List;

@Repository
@NoArgsConstructor
public class OrderItemRepository {

    @PersistenceContext
    private EntityManager em;

    public OrderItem save(OrderItem orderItem) {
        em.persist(orderItem);
        return orderItem;
    }

    public List<OrderItem> find(Order order) {
        return em.createQuery("select i from OrderItem i where i.order = : order", OrderItem.class)
                .setParameter("order", order)
                .getResultList();
    }


}
