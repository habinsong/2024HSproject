package shop.shopping.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.shopping.entity.Item;
import shop.shopping.formdto.ItemForm;
import shop.shopping.service.ItemService;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("ItemForm", new ItemForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String itemNew(@Valid ItemForm itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        // 상품 등록 시 필수 값이 없을 때 애러 발생
        if(bindingResult.hasErrors()){
            return "item/itemForm"; // 에러가 발생하면 상품 등록 get 페이지로 이동
        }

        // 상품 등록 시 첫번째 이미지가 없으면 애러 발생 (첫 번째 이미지는 대표 상품 이미지여서 꼭 있어야함!)
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getItemId() == null){

            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";// 에러가 발생하면 상품 등록 get 페이지로 이동
        }


        try { // 상품 저장 로직 호출
            itemService.saveItem(itemFormDto, itemImgFileList); // itemFormDto: 상품 정보, itemImgFileList: 상품 이미지 정보들 리스트
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/"; // 메인 페이지로 리다이렉트
    }

    @GetMapping(value = "/items/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemForm itemForm = itemService.getItemDetail(itemId);
            model.addAttribute("itemForm", itemForm);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemForm", new ItemForm());
            return "items/itemForm";
        }

        return "items/itemForm";
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
