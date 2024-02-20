package shop.shopping.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.Member;
import shop.shopping.domain_entity.OrderItem;
import shop.shopping.service.CartService.CartService;
import shop.shopping.service.ItemService.ItemImgService;
import shop.shopping.service.ItemService.ItemService;
import shop.shopping.service.OrderService.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final CartService cartService;
    private final ItemService itemService;
    private final ItemImgService itemImgService;
    private final OrderService orderService;
    String imgurl =  "/itemImgUpload/";

    @GetMapping("/Orders/order")
    public String OrderPage(HttpSession session, Model model) {
        Member member = (Member)session.getAttribute("userinfo");
        List<OrderItem> result= orderService.findById(member);
        Map<OrderItem, String> OrderitemUrlMap = new HashMap<>();
        for (OrderItem orderItem : result) {
            Item item = orderItem.getItem();
            String fileName = itemImgService.findFileName(item.getItemId());
            String url = imgurl + fileName;
            OrderitemUrlMap.put(orderItem, url);
        }
        model.addAttribute("OrderitemUrlMap", OrderitemUrlMap);
        return "/Orders/order";
    }


}
