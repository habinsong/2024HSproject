package shop.shopping.domain_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @Column(name="member_id")
    private String id;
    private String password;
    private String name;
    private String email;
    private String phoneNum;
    private String address;

}
