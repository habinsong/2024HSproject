package shop.shopping.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    private String id;
    private String password;
    private String name;
    private String email;
    private String phoneNum;
    private String address;

}
