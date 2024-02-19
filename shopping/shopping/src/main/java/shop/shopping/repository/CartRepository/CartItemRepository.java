package shop.shopping.repository.CartRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.CartItem;
import shop.shopping.formdto.CartDetailDto;

import java.util.List;
import java.util.Optional;

@Repository
@NoArgsConstructor
public class CartItemRepository {

    @PersistenceContext
    private EntityManager em;

    public CartItem save(CartItem cartItem) {
        em.persist(cartItem);
        return cartItem;
    }

    public CartItem delete(CartItem cartItem) {
        em.remove(cartItem);
        return cartItem;
    }

    public Optional<CartItem> findById(Long id) {
        CartItem cartItem = em.find(CartItem.class, id);
        return Optional.ofNullable(cartItem);
    }

    //현재 로그인한 회원의 Cart 엔티티를 찾기 위해
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId) {
        return em.createQuery("SELECT c FROM CartItem c WHERE c.cart.id = :cartId AND c.item.id = :itemId", CartItem.class)
                .setParameter("cartId", cartId)
                .setParameter("itemId", itemId)
                .getSingleResult();
    }

    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
        return em.createQuery("SELECT new shop.shopping.formdto.CartDetailDto(c.item.itemId, c.item.itemNm, c.item.price, c.count) FROM CartItem c WHERE c.cart.id = :cartId", CartDetailDto.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

}
