package shop.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import shop.shopping.entity.constant.ItemSellStatus;

@Entity
@Getter
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long itemId;

    private String itemNm;
    private int price;
    private int stock;
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //판매 상태[SELL, SOLD_OUT]

}
