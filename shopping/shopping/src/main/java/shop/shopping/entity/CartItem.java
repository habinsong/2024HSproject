package shop.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="cart_item")
public class CartItem {

    @Id @GeneratedValue
    @Column(name="cart_item_id")
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int count;

}
