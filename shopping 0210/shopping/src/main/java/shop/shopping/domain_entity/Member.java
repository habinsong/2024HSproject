package shop.shopping.domain_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    private String id;
    private String password;
    private String name;
    private String email;
    private String phoneNum;
    private String address;


}
