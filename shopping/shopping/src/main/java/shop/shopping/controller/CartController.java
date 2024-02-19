package shop.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import shop.shopping.domain_entity.Cart;
import shop.shopping.service.CartService.CartService;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;




}
