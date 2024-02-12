package shop.shopping.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.entity.Item;
import shop.shopping.entity.ItemImg;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemImgRepository {

    private final EntityManager em;

    public void save(ItemImg itemImg) {
        if(itemImg.getItemImgId() == null) {
            em.persist(itemImg);
        } else {
            em.merge(itemImg);
        }
    }

    /**
     * item 전체 찾기
     */
    public List<ItemImg> findAll() {
        return em.createQuery("select i from ItemImg i", ItemImg.class).getResultList();
    }

    /**
     * item 아이디로 찾기
     */
    public Optional<ItemImg> findById(Long itemImgId) {
        List<ItemImg> result = em.createQuery("select m from ItemImg m where m.itemImgId = :itemId", ItemImg.class)
                .setParameter("itemImgId", itemImgId)
                .getResultList();
        return result.stream().findAny();
    }

    public List<ItemImg> findByItemIdOrderByIdAsc(Long itemId) {
        if (itemId == null || itemId <= 0L) {
            return List.of(); // Return an empty list if 'itemId' is invalid
        }

        String queryString = "SELECT im FROM ItemImg im JOIN im.item i WHERE i.id = :itemId ORDER BY im.id ASC";
        return em.createQuery(queryString, ItemImg.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }


}
