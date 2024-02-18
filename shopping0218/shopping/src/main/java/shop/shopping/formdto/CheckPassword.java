package shop.shopping.formdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckPassword {
    private String password;
    private String new_pwd;
    private String new_pwd_again;
}
