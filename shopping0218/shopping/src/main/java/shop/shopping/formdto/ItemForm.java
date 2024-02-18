package shop.shopping.formdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.Member;
import shop.shopping.domain_entity.constant.ItemSellStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemForm {

    private Long itemId;

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력값입니다.")
    private Integer price;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    @NotBlank(message = "설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotBlank(message = "카테고리 선택은 필수 선택입니다")
    private String kategorie;

    private ItemSellStatus itemSellStatus;

    private Member member;
    
    //상품 업데이트 때 이미지 정보 저장
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    //수정할 때 이미지 아이디 저장
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemForm of(Item item){
        return modelMapper.map(item,ItemForm.class);
    }

//    //생성 메서드
//    public void createItemForm(Long itemId, String itemNm, int price, int stock, String itemDetail, ItemSellStatus itemSellStatus) {
//        this.itemId = itemId;
//        this.itemNm = itemNm;
//        this.price = price;
//        this.stock = stock;
//        this.itemDetail = itemDetail;
//        this.itemSellStatus = itemSellStatus;
//    }

}