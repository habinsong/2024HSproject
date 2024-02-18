package shop.shopping.repository.cartRepository;

import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.Member;

import java.util.Optional;

@Repository
@NoArgsConstructor
public class CartRepository {

    private EntityManager em;

    public Cart save(Cart cart) {
        em.persist(cart);
        return cart;
    }

    public Cart findByMemberId(String memberId) {
        return em.find(Cart.class, memberId);

    }

}
