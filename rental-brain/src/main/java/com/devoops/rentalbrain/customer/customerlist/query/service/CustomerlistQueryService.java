package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerKpiDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;

public interface CustomerlistQueryService {

    // 고객 목록 조회 (검색 + 페이징)
    PageResponseDTO<CustomerDTO> findAll(CustomerlistSearchDTO searchDTO);

    // 고객 상세 조회
    CustomerDetailResponseDTO findById(Long id);

    // KPI 조회 (신규 추가)
    CustomerKpiDTO getKpi();
}