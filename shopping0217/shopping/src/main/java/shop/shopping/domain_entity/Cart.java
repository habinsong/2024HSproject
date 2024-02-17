package shop.shopping.domain_entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Cart {
    @Id @GeneratedValue
    @Column(name="cart_id")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;
}
