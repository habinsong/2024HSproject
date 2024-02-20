package shop.shopping.service.CartService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.CartItem;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.Member;
import shop.shopping.formdto.CartDetailDto;
import shop.shopping.formdto.CartItemDto;
import shop.shopping.repository.CartRepository.CartItemRepository;
import shop.shopping.repository.CartRepository.CartRepository;
import shop.shopping.repository.ItemRepository.ItemRepository;
import shop.shopping.repository.MemberRepository.JpaMemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long join(Cart cart) {
        cartRepository.save(cart);
        return cart.getCartId();
    }

    public void deleteCart(String memberId) {
        Cart cart = cartRepository.findByMemberId(memberId);
        cartRepository.delete(cart);
    }

    public Cart findById(String id) {
        return cartRepository.findByMemberId(id);
    }

    public void addCart(CartItemDto cartItemDto, Item findItem) {
        CartItem cartItem = cartItemDto.CreateCartItem();
        int computeStock = findItem.getStock() - cartItem.getCount();
        findItem.setStock(computeStock);
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdItemId(cartItem.getCart(), cartItem.getItem());
        if(existingCartItem.isPresent()) {
            CartItem c = existingCartItem.get();
            c.setCount(c.getCount()+cartItem.getCount());
            cartItemRepository.save(c);
            itemRepository.save(findItem);
        }else {
            cartItemRepository.save(cartItem);
            itemRepository.save(findItem);
        }

    }

    //카트 조회
    public List<Item> getCartList(Member member) {
        Optional<Cart> cart = cartRepository.findByMember(member);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.get().getCartId());
        List<Item> userCartItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Long itemId = cartItem.getItem().getItemId();
            Optional<Item> item = itemRepository.findOne(itemId);
            item.get().setStock(cartItem.getCount());
            userCartItems.add(item.get());
        }
        return userCartItems;
    }

    public List<CartItem> getCartItems(Member member) {
        Optional<Cart> cart = cartRepository.findByMember(member);
        return cartItemRepository.findByCartId(cart.get().getCartId());
    }

    //delete
    public void removeItem(Cart cart) {
        cartItemRepository.remove(cart);
    }


}
