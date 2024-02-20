package shop.shopping.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Member;
import shop.shopping.domain_entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager em;

    public Order save(Order order) {
        em.persist(order);
        return order;
    }

    public Optional<Order> findOrderId(Member member) {
        List<Order> result = em.createQuery("select o from Order o where o.member = :member", Order.class)
                .setParameter("member", member)
                .getResultList();
        return result.stream().findAny();
    }


}
