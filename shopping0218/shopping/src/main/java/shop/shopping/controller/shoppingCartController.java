package shop.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class shoppingCartController {
    //장바구니 페이지로 이동
    @GetMapping("/shoppingCart/was_paid")
    public String was_paid() {
        return "/shoppingCart/was_paid";
    }
}
