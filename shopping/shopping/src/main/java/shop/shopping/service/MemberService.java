package shop.shopping.service;

import jakarta.transaction.Transactional;
import shop.shopping.controller.MemberLogin;
import shop.shopping.domain_entity.Member;
import shop.shopping.repository.MemberRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;


@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원가입기능
    public String join(Member member) {
        validateDuplicateMemberById(member); //중복 회원 검증
        validateDuplicateMemberByphoneNum(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMemberById(Member member) {
        memberRepository.findById(member.getId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 아이디입니다.");
                });
    }

    private void validateDuplicateMemberByphoneNum(Member member) {
        memberRepository.findByPhoneNum(member.getPhoneNum())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 휴대폰번호입니다.");
                });
    }

    //로그인 기능
    public MemberLogin login(MemberLogin login) {
        validateDuplicateMemberById_login(login);
        validateDuplicateMemberBypassword_login(login);
        return login;
    }

    private void validateDuplicateMemberById_login(MemberLogin login) {
        memberRepository.findById(login.getId())
                .orElseThrow(() -> new IllegalStateException("아이디나 비밀번호를 확인해주세요..."));
    }

    private void validateDuplicateMemberBypassword_login(MemberLogin login) {
        memberRepository.findByPassword(login.getPassword())
                .orElseThrow(() -> new IllegalStateException("아이디나 비밀번호를 확인해주세요..."));
    }



    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        // getter, setter
    }

    //회원정보 수정 기능
    //로그인된id를 통해서 회원의 정보를 반환
    public Optional<Member> userinfo(MemberLogin login) {
        return memberRepository.findMemberById(login.getId());
    }


}


