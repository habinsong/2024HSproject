package shop.shopping.formdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    private Long memberId;
    private String id;
    private String password;
    private String name;
    private String email;
    private String phoneNum;

}
