package com.devoops.rentalbrain.product.productlist.query.controller;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;
import com.devoops.rentalbrain.product.productlist.query.service.ItemQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("item")
public class ItemQueryController {
    final private ItemQueryService itemQueryService;

    @Autowired
    public ItemQueryController(ItemQueryService itemQueryService) {
        this.itemQueryService = itemQueryService;
    }

    @GetMapping("read-all/{itemName}")
    public ResponseEntity<List<EachItemDTO>> readAllItems(@PathVariable String itemName) {
        List<EachItemDTO> itemsList = itemQueryService.readAllItems(itemName);
        return ResponseEntity.ok().body(itemsList);
    }

    @GetMapping("read-groupby-name")
    public ResponseEntity<List<ItemNameDTO>> readItemsGroupByName() {
        List<ItemNameDTO> itemNameList = itemQueryService.readItemsGroupByName();
        return ResponseEntity.ok().body(itemNameList);
    }
}
