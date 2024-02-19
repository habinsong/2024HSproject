package shop.shopping.service.ItemService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.ItemImg;
import shop.shopping.formdto.ItemForm;
import shop.shopping.formdto.ItemImgDto;
import shop.shopping.repository.ItemRepository.ItemImgRepository;
import shop.shopping.repository.ItemRepository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

    /**
     * item 저장
     */
    @Transactional
    public Long saveItem(ItemForm itemForm, List<MultipartFile> itemImgFileList) throws Exception{
        Item item = itemForm.createItem();
        itemRepository.save(item);

        for(int i = 0; i < itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepImgYn("Y"); // 첫번째 이미지를 대표 상품 이미지로 설정
            else
                itemImg.setRepImgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 리스트 형태로 이미지들 저장
        }

        return item.getItemId();
    }

    public void updateItem(Long id, ItemForm itemForm, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        Optional<Item> item = itemRepository.findOne(id);
        item.ifPresent(i -> {
            i.setItemNm(itemForm.getItemNm());
            i.setItemDetail(itemForm.getItemDetail());
            i.setItemSellStatus(itemForm.getItemSellStatus());
            i.setKategorie(itemForm.getKategorie());
            i.setPrice(itemForm.getPrice());
            i.setStock(itemForm.getStock());
            itemRepository.save(i);
        });

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item.get());
            if(i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.updateItemImg(itemImg, itemImgFileList.get(i));
        }

    }

    public void deleteItem(Long itemId) {
        itemRepository.findOne(itemId).ifPresent(
                item -> {
                    itemImgService.deleteItemImg(item);
                    itemRepository.deleteItem(item);
                });
    }

    public List<Item> findAllItem() {
        return itemRepository.findAll();
    }

    public Optional<Item> findItemById(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> itemList(String id) {
        return itemRepository.findUserItem(id);
    }



}

//    /**
//     * item 찾기
//     */
//    public List<Item> findItems() {
//        return itemRepository.findAll();
//    }
//
//    /**
//     * item 전체 찾기
//     */
//    public Optional<Item> findOne(Long itemId) {
//        return itemRepository.findOne(itemId);
//    }

