package com.devoops.rentalbrain.customer.customerlist.query.service;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.common.pagination.Pagination;
import com.devoops.rentalbrain.common.pagination.PagingButtonInfo;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerKpiDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customerlist.query.mapper.CustomerlistQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerlistQueryServiceImpl implements CustomerlistQueryService {

    private final CustomerlistQueryMapper customerlistQueryMapper;

    @Override
    public PageResponseDTO<CustomerDTO> findAll(CustomerlistSearchDTO searchDTO) {
        int totalCount = customerlistQueryMapper.countCustomer(searchDTO);
        List<CustomerDTO> content = customerlistQueryMapper.selectCustomerList(searchDTO);
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(searchDTO, totalCount);
        return new PageResponseDTO<>(content, totalCount, paging);
    }

    @Override
    public CustomerDetailResponseDTO findById(Long id) {
        // ResultMap을 사용하여 한 번에 조회하므로 서비스 코드가 매우 간결해짐
        CustomerDetailResponseDTO detail = customerlistQueryMapper.selectCustomerDetail(id);
        if (detail == null) {
            throw new IllegalArgumentException("해당 고객이 존재하지 않습니다. ID=" + id);
        }
        return detail;
    }

    @Override
    public CustomerKpiDTO getKpi() {
        return customerlistQueryMapper.selectKpi();
    }
}