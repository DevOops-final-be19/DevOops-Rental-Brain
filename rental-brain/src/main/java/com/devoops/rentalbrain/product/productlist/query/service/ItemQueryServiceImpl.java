package com.devoops.rentalbrain.product.productlist.query.service;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import com.devoops.rentalbrain.product.productlist.query.dto.ItemNameDTO;
import com.devoops.rentalbrain.product.productlist.query.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemQueryServiceImpl implements ItemQueryService {
    final private ItemMapper itemMapper;

    @Autowired
    public ItemQueryServiceImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }


    @Override
    public List<EachItemDTO> readAllItems(String itemName) {
        List<EachItemDTO> itemsList = itemMapper.selectAllItems(itemName);

        return itemsList;
    }

    @Override
    public List<ItemNameDTO> readItemsGroupByName() {
        return List.of();
    }
}
