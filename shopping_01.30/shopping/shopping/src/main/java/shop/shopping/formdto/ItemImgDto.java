package shop.shopping.formdto;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import shop.shopping.entity.ItemImg;
import shop.shopping.entity.constant.RepImgYn;

@Getter
@Setter
public class ItemImgDto {

    private Long ItemId;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    // entity <-> dto
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }

}
