package shop.shopping.domain_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import shop.shopping.domain_entity.constant.ItemSellStatus;
import shop.shopping.exception.NotEnoughStockException;
import shop.shopping.formdto.ItemForm;

@Entity
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    private String itemNm;
    private int price;
    private int stock;
    private String itemDetail;
    private String kategorie;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //판매 상태[SELL, SOLD_OUT]

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stock += quantity;
    }

    /**
     *stock 감소
     */
    public void removeStock(int quantity) {
        int resultStock = this.stock - quantity;

        if(resultStock < 0) {
            throw new NotEnoughStockException("재고 수량이 0이하 입니다.");
        }
        this.stock = resultStock;
    }


}
