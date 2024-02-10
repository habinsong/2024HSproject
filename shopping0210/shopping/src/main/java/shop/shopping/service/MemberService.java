package shop.shopping.service;

import jakarta.transaction.Transactional;
import org.springframework.dao.DuplicateKeyException;
import shop.shopping.controller.MemberLogin;
import shop.shopping.domain_entity.Member;
import shop.shopping.repository.MemberRepository;

import javax.naming.Name;
import java.util.NoSuchElementException;
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
                    throw new DuplicateKeyException("이미 가입된 아이디입니다.");
                });
    }

    private void validateDuplicateMemberByphoneNum(Member member) {
        memberRepository.findByPhoneNum(member.getPhoneNum())
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 가입된 휴대폰번호입니다.");
                });
    }

    //로그인 기능
    public MemberLogin login(MemberLogin login) {
        checkDuplicateMemberById(login);
        checkDuplicateMemberBypassword(login);
        return login;
    }

    private void checkDuplicateMemberById(MemberLogin login) {
        memberRepository.findById(login.getId())
                .orElseThrow(() -> new NoSuchElementException("아이디나 비밀번호를 확인해주세요..."));
    }

    private void checkDuplicateMemberBypassword(MemberLogin login) {
        memberRepository.findByPassword(login.getPassword())
                .orElseThrow(() -> new NoSuchElementException("아이디나 비밀번호를 확인해주세요..."));
    }

    //회원정보 수정 기능
    //로그인된id를 통해서 회원의 정보를 반환
    public Optional<Member> userinfo(MemberLogin login) {
        return memberRepository.findMemberById(login.getId());
    }

    //이름변경기능
    public void updateName(String id, String newName){
        Optional<Member> m = memberRepository.findById(id);
        m.ifPresent(member -> {
            member.setName(newName);
            memberRepository.save(member);
        });
    }


    public void updateEmail(String id, String newEmail) {
        Optional<Member> m = memberRepository.findById(id);
        m.ifPresent(member -> {
            member.setEmail(newEmail);
            memberRepository.save(member);
        });
    }

    public void updateAddress(String id, String newAddress) {
        Optional<Member> m = memberRepository.findById(id);
        m.ifPresent(member -> {
            member.setAddress(newAddress);
            memberRepository.save(member);
        });
    }

    public void updatePassword(String id, String newPassword) {
        Optional<Member> m = memberRepository.findById(id);
        m.ifPresent(member -> {
            member.setPassword(newPassword);
            memberRepository.save(member);
        });
    }

    public void deleteMember(String id) {
        memberRepository.deleteInfo(id);
    }




}
