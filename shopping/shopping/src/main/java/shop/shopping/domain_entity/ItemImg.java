package shop.shopping.domain_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import shop.shopping.domain_entity.constant.RepImgYn;

@Entity
@Getter
@Setter
@Table(name="item_img")
public class ItemImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="item_img_id")
    private Long itemImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String imgName;
    private String oriImgName;
    private String imgUrl;

    private String repImgYn; //대표사진 [Y, N]

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
