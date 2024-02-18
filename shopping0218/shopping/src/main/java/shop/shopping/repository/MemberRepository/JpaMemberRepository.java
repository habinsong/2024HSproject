package shop.shopping.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaMemberRepository implements MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> deleteInfo(String id) {
        Member member = em.find(Member.class, id);
        em.remove(member);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findById(String id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByPhoneNum(String phoneNum) {
        List<Member> result = em.createQuery("select m from Member m where m.phoneNum = :phoneNum", Member.class)
                .setParameter("phoneNum", phoneNum)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByPassword(String password) {
        List<Member> result = em.createQuery("select m from Member m where m.password = :password", Member.class)
                .setParameter("password", password)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        List<Member> result = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByAddress(String address) {
        List<Member> result = em.createQuery("select m from Member m where m.address = :address", Member.class)
                .setParameter("address", address)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findMemberById(String id) {
        List<Member> result = em.createQuery("SELECT m FROM Member m WHERE m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList();

        return result.stream().findAny();
    }




}
