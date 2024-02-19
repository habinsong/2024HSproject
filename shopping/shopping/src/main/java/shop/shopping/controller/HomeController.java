package shop.shopping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import shop.shopping.domain_entity.Item;
import shop.shopping.service.ItemService.ItemImgService;
import shop.shopping.service.ItemService.ItemService;

import java.io.File;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Value("${itemImgLocation}")
    private String itemImgLocation;
    String imgurl =  "/itemImgUpload/";

    private final ItemService itemService;
    private final ItemImgService itemImgService;


    @GetMapping("/")
    public String home(Model model) {
        // 이미지 파일 리스트를 저장할 리스트 생성
        List<String> imageFileList = new ArrayList<>();
        try {
            // 폴더에서 모든 파일 목록 가져오기
            File folder = ResourceUtils.getFile(itemImgLocation);
            File[] files = folder.listFiles();
            // 파일 목록에서 이미지 파일인 것만 선택하여 리스트에 추가
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                    imageFileList.add(imgurl + file.getName());
                }
            }
            // 이미지 리스트의 크기를 저장
            int imageSize = imageFileList.size();
            // 이미지 주소를 담을 배열
            String[] randomImages = new String[imageSize];
            // 랜덤한 인덱스 생성 및 이미지 선택
            Random random = new Random();
            for (int i = 0; i < imageSize; i++) {
                // 랜덤한 인덱스 생성
                int randomIndex = random.nextInt(imageSize);
                // 이미 선택된 이미지 주소인 경우, 다시 랜덤한 인덱스 생성
                while (imageFileList.get(randomIndex) == null) {
                    randomIndex = random.nextInt(imageSize);
                }
                // 이미지 리스트에서 이미지 선택 및 이미지 리스트에서 해당 이미지 제거
                randomImages[i] = imageFileList.get(randomIndex);
                imageFileList.set(randomIndex, null);
            }
            // 선택된 이미지들을 모델에 추가
            for (int i = 0; i < imageSize; i++) {
                model.addAttribute("randomImage" + (i+1), randomImages[i]);
            }
        } catch (Exception e) {
            // 이미지를 불러오는 과정에서 예외가 발생하면 에러 메시지를 모델에 추가
            model.addAttribute("errorMessage", "이미지를 불러오는 중 오류가 발생하였습니다.");
            // 에러 페이지로 리다이렉트 또는 에러 메시지를 보여줄 뷰로 이동
            return "/index.html"; // 적절한 에러 처리 방법으로 변경하세요
        }
        List<Item>products = itemService.findAllItem();
        Map<Item, String> itemUrlMap = new HashMap<>();
        for (Item item : products) {
            Long itemId = item.getItemId();
            String fileName = itemImgService.findFileName(itemId);
            String url = imgurl + fileName;
            itemUrlMap.put(item, url);
        }
        model.addAttribute("itemUrlMap", itemUrlMap);
        // 랜덤한 이미지를 보여줄 뷰로 이동
        return "/index.html";
    }

}
