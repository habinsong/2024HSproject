package shop.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import shop.shopping.constant.RepImgYn;

@Entity
@Getter
@Table(name="item_img")
public class ItemImg {

    @Id @GeneratedValue
    @Column(name="item_img_id")
    private Long itemImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String imgName;
    private String orlImgName;
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private RepImgYn repImgYn; //대표사진 [Y, N]
}
