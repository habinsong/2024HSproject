package shop.shopping.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.shopping.domain_entity.Member;
import shop.shopping.service.MemberService;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //로그인/회원가입버튼 클릭시 로그인html로 이동하는 메서드
    @GetMapping("/members/login")
    public String memberlogin() {
        return "members/login";
    }

    //세션을 제거하고 로그아웃하는 메서드
    @GetMapping("/members/logout")
    public String memberlogout(HttpSession session) {
        session.invalidate();
        System.out.println("로그아웃 되었습니다");
        return "redirect:/";
    }

    //로그인화면에서 입력된 아이디와 비밀번호로 로그인하는 메서드
    @PostMapping("/members/login")
    public String login(MemberLogin login, HttpSession session) {
        MemberLogin log = memberService.login(login);
        if(log!=null) {
            session.setAttribute("loginPassword", log.getPassword());
            session.setAttribute("userinfo", memberService.userinfo(log).get()); //log값이 null이 아니면 옵셔널로 감싸진 memberService.userName(log)가 null값을 반환할리가 없어서 .get()메서드 바로 사용.
            System.out.println("로그인된 아이디 = "+log.getId());
            return "redirect:/";
        }
        else{
            System.out.println("로그인실패");
            return "redirect:/members/login";
        }
    }

    //로그인 페이지 밑의 회원가입 클릭시 회원가입창으로 이동하는 메서드
    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    //회원가입페이지로부터 정보를 입력받아 멤버객체에 먼저 저장하고 DB에 저장하는 메서드. 이때 정보를 콘솔창에 출력
    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(form.getPassword());
        member.setName(form.getName());
        member.setEmail(form.getEmail());
        member.setPhoneNum(form.getPhoneNum());
        member.setAddress(form.getAddress());

        System.out.println("Id = "+member.getId());
        System.out.println("Password = "+member.getPassword());
        System.out.println("Name = "+member.getName());
        System.out.println("Email = "+member.getEmail());
        System.out.println("PhoneNum = "+member.getPhoneNum());
        System.out.println("Address = "+member.getAddress());

        memberService.join(member);

        return "redirect:/";
    }

    //마이페이지 클릭시 페이지 이동 메서드
    @GetMapping("/members/Mypage")
    public String Mypage() {
        return "members/my_page";
    }

    //마이페이지에서 비밀번호를 다시 확인할 때 올바르게 입력되면 회원수정페이지로 이동하고 아니면 모델의 키값을 true로 설정한뒤 모델값이 true일때 경고창이 뜨도록 하는 메서드
    //이때 비밀번호 확인은 my_page에서만 사용되기 때문에 session이 아닌 model에 저장
    @PostMapping("/members/Mypage")
    public String CheckPassword(CheckPassword password, HttpSession session, Model model) {
        model.addAttribute("InputPassword", password.getPassword());
        if(session.getAttribute("loginPassword").equals(model.getAttribute("InputPassword"))) {
            return "members/editInfo";
        }
        else {
            model.addAttribute("passwordMismatch", true);
            return "members/my_page";
        }

    }

    //장바구니 페이지로 이도
    @GetMapping("/members/was_paid")
    public String was_paid() {
        return "/members/was_paid";
    }

    //이름변경 메서드
    @PostMapping("/members/editName")
    public String editName(MemberForm form, HttpSession session, Model model) {
        Member userinfo = (Member)session.getAttribute("userinfo");
        String id = userinfo.getId();
        String name = form.getName();
        if(userinfo.getName().equals(name)){
            model.addAttribute("validateDuplicateName", true);
            return "members/editInfo";
        }
        else {
            memberService.updateName(id, name);
            model.addAttribute("successEdit", true);
            model.addAttribute("editInfo", "이름");
            memberlogout(session);
            return "members/alertMessage";
        }
    }

    //이메일주소 변경 메서드
    @PostMapping("/members/editEmail")
    public String editEmail(MemberForm form, HttpSession session, Model model) {
        Member userinfo = (Member)session.getAttribute("userinfo");
        String id = userinfo.getId();
        String email = form.getEmail();
        if(userinfo.getEmail().equals(email)){
            model.addAttribute("validateDuplicateEmail", true);
            return "members/editInfo";
        }
        else {
            memberService.updateEmail(id, email);
            model.addAttribute("successEdit", true);
            model.addAttribute("editInfo", "이메일");
            memberlogout(session);
            return "members/alertMessage";
        }
    }

    //배송지 주소 변경 메서드
    @PostMapping("/members/editAddress")
    public String editAddress(MemberForm form, HttpSession session, Model model) {
        Member userinfo = (Member)session.getAttribute("userinfo");
        String id = userinfo.getId();
        String address = form.getAddress();
        if(userinfo.getAddress().equals(address)){
            model.addAttribute("validateDuplicateAddress", true);
            return "members/editInfo";
        }
        else {
            memberService.updateAddress(id, address);
            model.addAttribute("successEdit", true);
            model.addAttribute("editInfo", "배송지 주소");
            memberlogout(session);
            return "members/alertMessage";
        }
    }

    //패스워드 변경 메서드, 현재 비밀번호 확인 및 새비밀번호 재확인 후 조건에 맞으면 변경가능
    @PostMapping("/members/editPassword")
    public String editPassword(CheckPassword ckp, HttpSession session, Model model) {
        Member userinfo = (Member)session.getAttribute("userinfo");
        String id = userinfo.getId();
        String password = ckp.getPassword();
        String newPassword = ckp.getNew_pwd();
        String checkPassword = ckp.getNew_pwd_again();
        if(!userinfo.getPassword().equals(newPassword) && userinfo.getPassword().equals(password) && newPassword.equals(checkPassword)){
            memberService.updatePassword(id,newPassword);
            model.addAttribute("successEdit", true);
            model.addAttribute("editInfo", "비밀번호");
            memberlogout(session);
            return "members/alertMessage";
        } else if (!userinfo.getPassword().equals(password)) {
            model.addAttribute("validateDuplicatePassword", true);
            return "members/editInfo";
        } else if (userinfo.getPassword().equals(newPassword)) {
            model.addAttribute("oldPWD", true);
            return "members/editInfo";
        } else {
            model.addAttribute("validateDuplicateNewPassword", true);
            return "members/editInfo";
        }
    }

    //회원탈퇴 버튼 클릭시 회원탈퇴 페이지로 이동
    @GetMapping("/members/withdrawal")
    public String deleteMember() {
        return "/members/deleteMember";
    }

    @PostMapping("/members/withdrawal")
    public String checkForDelete(CheckPassword check, HttpSession session, Model model) {
        Member userinfo = (Member)session.getAttribute("userinfo");
        String password = check.getPassword();
        if(userinfo.getPassword().equals(password)) {
            model.addAttribute("CorrectPW", true);
            return "members/deleteMember";
        } else {
            model.addAttribute("notCorrectPW", true);
            return "members/deleteMember";
        }
    }

    @PostMapping("/members/DELETEINFO")
    public String DELETEINFO(HttpSession session, Model model) {
        Member userinfo = (Member) session.getAttribute("userinfo");
        String Id = userinfo.getId();
        memberService.deleteMember(Id);
        model.addAttribute("successDelete", true);
        memberlogout(session);
        return "/members/alertMessage";
    }





}
