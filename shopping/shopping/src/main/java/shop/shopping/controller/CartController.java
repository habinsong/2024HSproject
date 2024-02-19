package shop.shopping.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.Member;
import shop.shopping.formdto.CartItemDto;
import shop.shopping.service.CartService.CartService;
import shop.shopping.service.ItemService.ItemImgService;
import shop.shopping.service.ItemService.ItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ItemService itemService;
    private final ItemImgService itemImgService;
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

//    @PostMapping("/item/addCart/{itemId}")
//    public String addCartItem(@PathVariable("itemId")Long itemId, HttpSession session, CartItemDto cartItemDto){
//
//    }


}
