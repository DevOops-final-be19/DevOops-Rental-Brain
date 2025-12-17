package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.entity.CustomerlistCommandEntity;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerlistCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerlistCommandServiceImpl implements CustomerlistCommandService {

    private final CustomerlistCommandRepository customerlistCommandRepository;

    @Override
    public void create(CustomerlistCommandDTO dto) {
        // ... (생성 로직, ModelMapper 사용 가정)
        CustomerlistCommandEntity entity = new CustomerlistCommandEntity();
        // entity 값 설정 ...
        customerlistCommandRepository.save(entity);
    }

    @Override
    public void update(Long id, CustomerlistCommandDTO dto) {
        CustomerlistCommandEntity entity = customerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));
        // entity 값 업데이트 ...
    }

    @Override
    public void delete(Long id) {
        CustomerlistCommandEntity entity = customerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));
        entity.setIsDeleted("Y"); // 엔티티에 setter 혹은 delete() 메서드 필요
    }

    @Override
    public void restore(Long id) {
        CustomerlistCommandEntity entity = customerlistCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));
        entity.setIsDeleted("N"); // 복구
    }
}