package shop.shopping.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.shopping.entity.Item;
import shop.shopping.entity.ItemImg;
import shop.shopping.formdto.ItemForm;
import shop.shopping.formdto.ItemImgDto;
import shop.shopping.repository.ItemImgRepository;
import shop.shopping.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

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

    public ItemForm getItemDetail(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findOne(itemId).orElseThrow(EntityNotFoundException::new);
        ItemForm itemForm = ItemForm.of(item);
        itemForm.setItemImgDtoList(itemImgDtoList);

        return itemForm;
    }

    public Long updateItem(ItemForm itemForm, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        Item item = itemRepository.findOne(itemForm.getItemId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemForm);

        List<Long> itemImgIds = itemForm.getItemImgIds();

        //이미지 등록
        for (int i = 0, max = itemImgFileList.size(); i < max; i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getItemId();
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

