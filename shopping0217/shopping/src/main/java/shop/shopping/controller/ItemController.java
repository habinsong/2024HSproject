package shop.shopping.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import shop.shopping.domain_entity.Item;
import shop.shopping.domain_entity.Member;
import shop.shopping.formdto.ItemForm;
import shop.shopping.repository.ItemRepository.ItemRepository;
import shop.shopping.service.ItemService.ItemImgService;
import shop.shopping.service.ItemService.ItemService;

import java.io.File;
import java.util.*;


@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemImgService itemImgService;
    String imgurl =  "/itemImgUpload/";

    @GetMapping("/item/MyItem")
    public String MyItem() {
        return "/item/MyItem";
    }

    @GetMapping("/item/new")
    public String createForm() {
        return "item/createItemForm";
    }

    @PostMapping("/item/create")
    public String itemNew(@Valid ItemForm itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors",true);
            return "Message/alertMessage";
        }
        else {
            try { // 상품 저장 로직 호출
                Member member = (Member)session.getAttribute("userinfo");
                itemFormDto.setMember(member);
                itemService.saveItem(itemFormDto, itemImgFileList); // itemFormDto: 상품 정보, itemImgFileList: 상품 이미지 정보들 리스트
                model.addAttribute("successAddItem",true);
                return "Message/alertMessage";
            } catch (Exception e) {
                return "Message/errorView";
            }
        }

    }

    @GetMapping("/item/item_info")
    public String itemInfo() {
        return "item/item_info";
    }

    @GetMapping(value = "/item/item_info/{itemId}")
    public String getItemInfo(@PathVariable("itemId")Long itemId, Model model) {
        Item findItem = itemService.findItemById(itemId);
        String fileName = itemImgService.findFileName(itemId);
        String url = imgurl + fileName;

        List<Item>product = itemService.findAllItem();
        Map<Item, String> itemUrlMap = new HashMap<>();
        for (Item item : product) {
            Long Id = item.getItemId();
            String fileNm = itemImgService.findFileName(Id);
            String source = imgurl + fileNm;
            itemUrlMap.put(item, source);
        }
        model.addAttribute("itemUrlMap", itemUrlMap);
        model.addAttribute("item",findItem);
        model.addAttribute("imgUrl",url);
        return "item/item_info";
    }

    @GetMapping("/item/itemUpdate")
    public String itemUpdate(HttpSession session, Model model) {
        Member member = (Member)session.getAttribute("userinfo");
        String id = member.getId();
        List<Item> itemList = itemService.itemList(id);
        model.addAttribute("itemList", itemList);
        return "item/editItem";
    }

    @PostMapping(value = "/item/{itemId}")
    public String itemUpdate(@Valid ItemForm itemForm, BindingResult bindingResult
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {

        if (bindingResult.hasErrors()) {
            return "items/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemForm.getItemId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "items/itemForm";
        }

        try {
            itemService.saveItem(itemForm, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "items/itemForm";
        }

        return "redirect:/";
    }





}
