package shop.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.shopping.entity.Member;
import shop.shopping.formdto.MemberForm;

@Controller
public class MemberController {

    //private final MemberService memberService;

//    @Autowired
//    public MemberController(MemberService memberService) {
//        this.memberService = memberService;
//    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(form.getPassword());
        member.setName(form.getName());
        member.setEmail(form.getEmail());
        member.setPhoneNum(form.getPhoneNum());

        System.out.println("Id = "+member.getId());
        System.out.println("Password = "+member.getPassword());
        System.out.println("Name = "+member.getName());
        System.out.println("Email = "+member.getEmail());
        System.out.println("PhoneNum = "+member.getPhoneNum());

        //memberService.join(member);

        return "redirect:/";
    }


}
