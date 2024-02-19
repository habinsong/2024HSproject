package shop.shopping.service.ItemService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.ItemImg;
import shop.shopping.repository.ItemRepository.ItemImgRepository;
import shop.shopping.repository.ItemRepository.ItemRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = itemImgLocation + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

        Item item = itemImg.getItem();
        Long itemImgId = itemImgRepository.findItemImgByItem(item).get().getItemImgId();

        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) { //값이 null이거나 공백문자이면 true 값을 반환
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = itemImgLocation + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            itemImgRepository.save(savedItemImg);

        }
    }

    public String findFileName(Long lg) {
        Optional<ItemImg> findImg = itemImgRepository.findById(lg);
        if (findImg.isPresent()) {
            ItemImg itemImg = findImg.get();
            return itemImg.getImgName();
        }else {
            return null;
        }
    }

    public void deleteItemImg(Item item) {
        itemImgRepository.findItemImgByItem(item).ifPresent(itemImg -> {
            itemImgRepository.deleteImg(itemImg);
            try {
                fileService.deleteFile(itemImg.getImgUrl());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }





}