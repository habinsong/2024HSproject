package shop.shopping.formdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import shop.shopping.domain_entity.Cart;
import shop.shopping.domain_entity.CartItem;
import shop.shopping.domain_entity.Item;

@Getter
@Setter
public class CartItemDto {
    private static ModelMapper modelMapper = new ModelMapper();

    @NotNull
    private Long itemId;

    @Min(value = 1)
    private int count;

    @NotNull
    private Cart cart;

    public CartItem CreateCartItem(){
        return modelMapper.map(this, CartItem.class);
    }



}
