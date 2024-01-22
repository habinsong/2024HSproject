package shop.shopping.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.shopping.constant.Role;

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
