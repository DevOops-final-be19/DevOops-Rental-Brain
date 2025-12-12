package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.customersupport.command.dto.CustomersupportDTO;
import com.devoops.rentalbrain.customer.customersupport.command.service.CustomersupportCommandCustomersupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customersupports")
@Slf4j
public class CustomersupportCommandCustomersupportController {

    private final CustomersupportCommandCustomersupportService commandService;

    // 문의 등록
    @PostMapping("/insertSupport")
    public ResponseEntity<String> register(@RequestBody CustomersupportDTO dto) {
        Long savedId = commandService.registerSupport(dto);
        log.info("문의 등록 완료 ID: {}", savedId);
        return ResponseEntity.ok("문의가 정상적으로 등록되었습니다. (ID: " + savedId + ")");
    }

    // 문의 수정
    @PutMapping("/updateSupport/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CustomersupportDTO dto) {
        commandService.updateSupport(id, dto);
        log.info("문의 수정 완료 ID: {}", id);
        return ResponseEntity.ok("문의가 수정되었습니다.");
    }

    // 문의 삭제
    @DeleteMapping("/deleteSupport/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteSupport(id);
        log.info("문의 삭제 완료 ID: {}", id);
        return ResponseEntity.ok("문의가 삭제되었습니다.");
    }
}