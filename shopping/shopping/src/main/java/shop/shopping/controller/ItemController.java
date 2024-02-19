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

    @GetMapping(value = "/item/item_info/{itemId}")
    public String getItemInfo(@PathVariable("itemId")Long itemId, Model model) {
        Optional<Item> find = itemService.findItemById(itemId);
        Item findItem = find.get();
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
    public String showMyItemPage(HttpSession session, Model model) {
        Member member = (Member)session.getAttribute("userinfo");
        String id = member.getId();
        List<Item> itemList = itemService.itemList(id);
        Map<Item, String> itemUrlMap = new HashMap<>();
        for (Item item : itemList) {
            Long itemId = item.getItemId();
            String fileName = itemImgService.findFileName(itemId);
            String url = imgurl + fileName;
            itemUrlMap.put(item, url);
        }
        model.addAttribute("itemUrlMap", itemUrlMap);
        return "item/editItem";
    }

    @GetMapping("/item/item_update/{itemId}")
    public String itemUpdate(@PathVariable("itemId")Long itemId, Model model) {
        Optional<Item> find = itemService.findItemById(itemId);
        Item findItem = find.get();
        String fileName = itemImgService.findFileName(itemId);
        String url = imgurl + fileName;
        model.addAttribute("findItem", findItem);
        model.addAttribute("imgUrl", url);
        return "item/item_update";
    }

    @PostMapping("/item/itemUpdate/{itemId}")
    public String itemUpdate(@PathVariable("itemId")Long itemId, @Valid ItemForm itemForm, BindingResult bindingResult
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors",true);
            return "Message/alertMessage";
        }

        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("hasErrors",true);
            return "Message/alertMessage";
        }

        try {
            itemService.updateItem(itemId, itemForm, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("hasErrors",true);
            return "Message/alertMessage";
        }
        model.addAttribute("successEditItem",true);
        return "Message/alertMessage";
    }

    @PostMapping("/item/itemDelete/{itemId}")
    public String itemDelete(@PathVariable("itemId")Long itemId, Model model) {
        itemService.deleteItem(itemId);
        model.addAttribute("deleteMessage", true);
        return "Message/alertMessage";
    }






}
