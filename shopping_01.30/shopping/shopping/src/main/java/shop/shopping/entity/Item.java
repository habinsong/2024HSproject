package shop.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import shop.shopping.formdto.ItemForm;
import shop.shopping.entity.constant.ItemSellStatus;
import shop.shopping.exception.NotEnoughStockException;

@Entity
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long itemId;

    private String itemNm;
    private int price;
    private int stock;
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //판매 상태[SELL, SOLD_OUT]

    /**
     * stock 증가
     */
    public void addStick(int quantity) {
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
    
    //create 메서드
    public void createItem(ItemForm itemForm) {
        this.itemNm = itemForm.getItemNm();
        this.price = itemForm.getPrice();
        this.stock = itemForm.getStock();
        this.itemDetail = itemForm.getItemDetail();
        this.itemSellStatus = itemForm.getItemSellStatus();

    }

    //update 매세드
    public void updateItem(ItemForm itemForm) {
        this.itemNm = itemForm.getItemNm();
        this.price = itemForm.getPrice();
        this.stock = itemForm.getStock();
        this.itemDetail = itemForm.getItemDetail();
        this.itemSellStatus = itemForm.getItemSellStatus();
    }

}
