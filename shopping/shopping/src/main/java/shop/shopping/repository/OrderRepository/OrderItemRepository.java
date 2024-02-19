package shop.shopping.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemRepository {

    @PersistenceContext
    private EntityManager em;
}
