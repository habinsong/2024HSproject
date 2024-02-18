package shop.shopping.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Item;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * item 저장
     */
    public void save(Item item) {
        if(item.getItemId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    /**
     * item id찾기
     */
    public Optional<Item> findOne(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    /**
     * item 이름으로 찾기
     */
    public Optional<Item> findByName(String itemNm) {
        List<Item> result = em.createQuery("select m from Item m where m.itemNm = :itemNm", Item.class)
                .setParameter("itemNm", itemNm)
                .getResultList();
        return result.stream().findAny();
    }

    /**
     * item 전체 찾기
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
    /**
     * user의 item 찾기
     */
    public List<Item> findUserItem(String id) {
        List<Item> findItem = em.createQuery("select i from Item i where i.member.id = :id", Item.class)
                .setParameter("id", id)
                .getResultList();
        return findItem;
    }


}
