package com.devoops.rentalbrain.product.productlist.query.mapper;

import com.devoops.rentalbrain.product.productlist.query.dto.EachItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    List<EachItemDTO> selectAllItems(String itemName);
}
