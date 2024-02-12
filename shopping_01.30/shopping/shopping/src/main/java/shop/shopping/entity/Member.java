package shop.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private String memberId;

    private String id;
    private String password;
    private String name;
    private String email;
    private String phoneNum;

}
