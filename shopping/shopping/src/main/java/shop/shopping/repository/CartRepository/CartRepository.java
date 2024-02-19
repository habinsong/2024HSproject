package shop.shopping.repository.CartRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.Member;

import java.util.List;
import java.util.Optional;

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

    public Optional<Cart> findByMember(Member member) {
        List<Cart> result = em.createQuery("select c from Cart c where c.member =:member", Cart.class)
                .setParameter("member", member)
                .getResultList();
        return result.stream().findAny();
    }

}
