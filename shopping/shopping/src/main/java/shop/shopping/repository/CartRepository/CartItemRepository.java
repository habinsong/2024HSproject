package shop.shopping.repository.CartRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.CartItem;
import shop.shopping.domain_entity.Item;

import java.util.List;
import java.util.Optional;

@Repository
@NoArgsConstructor
public class CartItemRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(CartItem cartItem) {
        if(cartItem.getCartItemId() == null) {
            em.persist(cartItem);
        } else {
            em.merge(cartItem);
        }
    }

    public void remove(Cart cart) {
        em.createQuery("delete from CartItem c where c.cart = :cart")
                .setParameter("cart", cart)
                .executeUpdate();
    }

    public Optional<CartItem> findById(Long id) {
        CartItem cartItem = em.find(CartItem.class, id);
        return Optional.ofNullable(cartItem);
    }

    //현재 로그인한 회원의 Cart 엔티티를 찾기 위해
    public List<CartItem> findByCartId(Long cartId) {
        return em.createQuery("SELECT c FROM CartItem c WHERE c.cart.id = :cartId", CartItem.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    public Optional<CartItem> findByCartIdItemId(Cart cart, Item item) {
       List<CartItem> result = em.createQuery("select c from CartItem c where c.item = :item and c.cart = :cart", CartItem.class)
                .setParameter("item", item)
                .setParameter("cart", cart)
                .getResultList();
       return result.stream().findAny();
    }

}
