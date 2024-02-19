package shop.shopping.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.ItemImg;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemImgRepository {

    @PersistenceContext
    private EntityManager em;

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
        List<ItemImg> result = em.createQuery("select m from ItemImg m where m.itemImgId = :itemImgId", ItemImg.class)
                .setParameter("itemImgId", itemImgId)
                .getResultList();
        return result.stream().findAny();
    }

    public Optional<ItemImg> findItemImgByItem(Item item) {
        List<ItemImg> result = em.createQuery("select i from ItemImg i where i.item = :item", ItemImg.class)
                .setParameter("item", item)
                .getResultList();
        return result.stream().findAny();
    }


    public void deleteImg(ItemImg itemImg) {
        em.remove(itemImg);
    }

}
