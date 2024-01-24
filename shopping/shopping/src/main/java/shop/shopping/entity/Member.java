package shop.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import shop.shopping.entity.constant.Role;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long memberId;

    private String id;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role; //등급 [USER, ADMIN]

    private String email;
    private String address;
    private String phoneNum;


}
