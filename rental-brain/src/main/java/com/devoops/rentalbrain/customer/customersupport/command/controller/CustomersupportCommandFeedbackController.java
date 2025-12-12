package com.devoops.rentalbrain.customer.customersupport.command.controller;

import com.devoops.rentalbrain.customer.customersupport.command.dto.FeedbackDTO;
import com.devoops.rentalbrain.customer.customersupport.command.service.CustomersupportCommandFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
@Slf4j
public class CustomersupportCommandFeedbackController {

    private final CustomersupportCommandFeedbackService commandService;

    // 피드백 등록
    @PostMapping("/insertFeedback")
    public ResponseEntity<String> register(@RequestBody FeedbackDTO dto) {
        Long savedId = commandService.registerFeedback(dto);
        log.info("피드백 등록 완료 ID: {}", savedId);
        return ResponseEntity.ok("피드백이 정상적으로 등록되었습니다. (ID: " + savedId + ")");
    }

    // 피드백 수정
    @PutMapping("/updateFeedback/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody FeedbackDTO dto) {
        commandService.updateFeedback(id, dto);
        log.info("피드백 수정 완료 ID: {}", id);
        return ResponseEntity.ok("피드백이 수정되었습니다.");
    }

    // 피드백 삭제
    @DeleteMapping("/deleteFeedback/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commandService.deleteFeedback(id);
        log.info("피드백 삭제 완료 ID: {}", id);
        return ResponseEntity.ok("피드백이 삭제되었습니다.");
    }
}