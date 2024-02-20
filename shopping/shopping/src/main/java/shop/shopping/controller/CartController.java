package shop.shopping.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.shopping.domain_entity.*;
import shop.shopping.domain_entity.constant.OrderStatus;
import shop.shopping.formdto.CartItemDto;
import shop.shopping.service.CartService.CartService;
import shop.shopping.service.ItemService.ItemImgService;
import shop.shopping.service.ItemService.ItemService;
import shop.shopping.service.OrderService.OrderService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ItemService itemService;
    private final ItemImgService itemImgService;
    private final OrderService orderService;
    String imgurl =  "/itemImgUpload/";

    @GetMapping("/shoppingCart/was_paid")
    public String was_paid(HttpSession session, Model model) {
        Member member = (Member)session.getAttribute("userinfo");
        List<Item> itemList = cartService.getCartList(member);
        Map<Item, String> itemUrlMap = new HashMap<>();
        for (Item item : itemList) {
            Long itemId = item.getItemId();
            String fileName = itemImgService.findFileName(itemId);
            String url = imgurl + fileName;
            itemUrlMap.put(item, url);
        }
        model.addAttribute("itemUrlMap", itemUrlMap);
        return "/shoppingCart/was_paid";
    }

    @PostMapping("/item/addCart/{itemId}")
    public String addCartItem(@PathVariable("itemId")Long itemId, HttpSession session, CartItemDto cartItemDto, Model model){
        Optional<Item> item = itemService.findItemById(itemId);
        Item findItem = new Item();
        if (item.isPresent()) {
            findItem = item.get();
        }
        int computeStock = findItem.getStock() - cartItemDto.getCount();

        try {
            if (findItem.getStock() > 0 && computeStock >= 0) {
                Member member = (Member)session.getAttribute("userinfo");
                Cart cart = cartService.findById(member.getId());
                cartItemDto.setItemId(itemId);
                cartItemDto.setCart(cart);
                cartService.addCart(cartItemDto, findItem);
                model.addAttribute("successAddCartItem", true);
                return "Message/alertMessage";
            } else {
                model.addAttribute("failedAddCartItem", true);
                return "Message/alertMessage";
            }
        }catch (Exception e) {
            model.addAttribute("errorMessage","로그인 후 이용해주세요.");
            return "/Message/errorView";
        }
    }

    @PostMapping("/shoppingCart/removeCartItem")
    public String removeCartItem(HttpSession session, Model model) {
        Member member = (Member)session.getAttribute("userinfo");
        Cart cart = cartService.findById(member.getId());
        cartService.removeItem(cart);
        model.addAttribute("removeItem", true);
        return "Message/alertMessage";
    }

    @PostMapping("/shoppingCart/buyItems")
    @Transactional
    public String buyItems(HttpSession session, Model model) {

        try {
            Member member = (Member) session.getAttribute("userinfo");
            LocalDateTime orderDate = LocalDateTime.now();
            Order order = Order.createOrder(member, orderDate, OrderStatus.ORDER);
            orderService.joinOrder(order);

            List<CartItem> result = cartService.getCartItems(member);
            for (CartItem cartItem : result) {
                Item item = cartItem.getItem();
                int quantity = cartItem.getCount();
                int price = item.getPrice() * cartItem.getCount();
                OrderItem orderItem = OrderItem.createOrderItem(item, order, price, quantity);
                orderService.joinOrderItem(orderItem);
            }
            cartService.removeItem(cartService.findById(member.getId()));
            model.addAttribute("successOrder", true);
            return "Message/alertMessage";
        }catch (Exception e) {
            model.addAttribute("errorMessage","오류가 발생했습니다.");
            return "/Message/errorView";
        }

    }



}
