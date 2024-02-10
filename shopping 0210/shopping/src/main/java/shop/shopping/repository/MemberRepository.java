package shop.shopping.repository;

import shop.shopping.domain_entity.Member;

import java.util.Optional;

public interface MemberRepository {
    //구현하려는 기능 --> 회원등록, 회원수정, 상품등록, 상품조회, 상품수정, 장바구니담기, 장바구니구매, 장바구니취소...
    Member save(Member member);
    Optional<Member> findById(String id);
    Optional<Member> findByPhoneNum(String phoneNum);
    Optional<Member> findByPassword(String password);
    Optional<Member> findByName(String name);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByAddress(String address);
    Optional<Member> findMemberById(String id);


}
