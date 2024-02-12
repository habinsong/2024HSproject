package shop.shopping.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.shopping.entity.Item;
import shop.shopping.entity.ItemImg;
import shop.shopping.entity.constant.ItemSellStatus;
import shop.shopping.formdto.ItemForm;
import shop.shopping.repository.ItemImgRepository;
import shop.shopping.repository.ItemRepository;
import shop.shopping.service.ItemService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {"spring.config.location=classpath:applicationTest.yml"})
class ItemTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        //path 제일 앞에 경로를 /User로 수정함
        for (int i = 0; i < 5; i++) {
            String path = "E:\\이경주\\쇼핑몰\\shopping_01.30\\shopping\\shopping\\itemImgUpload";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    public void saveItem() throws Exception {
        ItemForm itemFormDto = new ItemForm(); // 상품 등록 화면에서 입력 받는 상품 데이터 setter
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStock(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();

        // 상품 데이터와 이미지 정보를 파라미터로 넘겨서 저장 후 저장된 상품의 아이디 값을 반환
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
        List<ItemImg> itemImgList = itemImgRepository.findAll();

        Item item = itemRepository.findOne(itemId);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStock(), item.getStock());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName()); // 상품 이미지는 첫번째 파일의 원본 이미지 파일 이름만 같은지 확인함
    }

}