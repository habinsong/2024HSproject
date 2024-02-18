package shop.shopping.service.cartService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.CartItem;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.Member;
import shop.shopping.formdto.CartDetailDto;
import shop.shopping.formdto.CartItemDto;
import shop.shopping.repository.ItemRepository.ItemRepository;
import shop.shopping.repository.MemberRepository.JpaMemberRepository;
import shop.shopping.repository.MemberRepository.MemberRepository;
import shop.shopping.repository.cartRepository.CartItemRepository;
import shop.shopping.repository.cartRepository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String id) {
        //장바구니에 담을 상품 조회, 로그인한 회원 조회
        Item item = itemRepository.findOne(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = jpaMemberRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원 장바구니 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null) {
            Cart.createCart(member);
            cartRepository.save(cart);
        }
        
        //상품이 들어가 있는지 획인 후 있으면 + 없으면 저장
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getCartId(), item.getItemId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getCartItemId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getCartItemId();
        }
    }

    //카트 조회
    public List<CartDetailDto> getCartList(String id) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = jpaMemberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("아이디나 비밀번호를 확인해주세요..."));
        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart == null){ // 
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getCartId());
        return cartDetailDtoList; // 카트 있으면은 cartItemRepository 의 JPQL 쿼리로 걸러진 아이템들을 담영서 반환
    }

    //update
    public void updateCartItemCount(Long cartItemId, int count){

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    //delete
    public void deleteCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItemRepository.delete(cartItem);
    }

}
