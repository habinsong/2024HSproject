package shop.shopping.formdto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
