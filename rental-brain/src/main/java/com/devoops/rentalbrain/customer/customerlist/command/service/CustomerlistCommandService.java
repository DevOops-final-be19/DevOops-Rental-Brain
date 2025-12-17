package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;

public interface CustomerlistCommandService {
    void create(CustomerlistCommandDTO dto);
    void update(Long id, CustomerlistCommandDTO dto);
    void delete(Long id);
    void restore(Long id); // 복구 메서드 추가
}