package com.devoops.rentalbrain.customer.customerlist.query.mapper;

import com.devoops.rentalbrain.business.contract.query.dto.ContractSummaryDTO;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerKpiDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customersupport.command.dto.CustomersupportDTO;
import com.devoops.rentalbrain.product.maintenance.query.dto.AfterServiceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CustomerlistQueryMapper {
    List<CustomerDTO> selectCustomerList(@Param("dto") CustomerlistSearchDTO dto);
    int countCustomer(@Param("dto") CustomerlistSearchDTO dto);
    CustomerKpiDTO selectKpi();

    // ResultMap 사용 메서드
    CustomerDetailResponseDTO selectCustomerDetail(Long id);

    // Collection 매핑용 서브 메서드들
    List<CustomersupportDTO> selectSupportsByCustomerId(Long id);
    List<ContractSummaryDTO> selectContractsByCustomerId(Long id);
    List<AfterServiceDTO> selectAsByCustomerId(Long id);
}