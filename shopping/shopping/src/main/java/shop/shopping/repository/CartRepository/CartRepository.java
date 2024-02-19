package shop.shopping.repository.CartRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Cart;

@Repository
@NoArgsConstructor
public class CartRepository {

    @PersistenceContext
    private EntityManager em;

    public Cart save(Cart cart) {
        em.persist(cart);
        return cart;
    }

    public void delete(Cart cart) {
        em.remove(cart);
    }

    public Cart findByMemberId(String memberId) {
        return em.find(Cart.class, memberId);

    }

}
