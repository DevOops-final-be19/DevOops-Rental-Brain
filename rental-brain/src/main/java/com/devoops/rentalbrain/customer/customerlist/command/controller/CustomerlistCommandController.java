package com.devoops.rentalbrain.customer.customerlist.command.controller;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerlistCommandDTO;
import com.devoops.rentalbrain.customer.customerlist.command.service.CustomerlistCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "고객 관리(Command)", description = "고객 등록, 수정, 삭제, 복구 관련 명령 API")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerlistCommandController {

    private final CustomerlistCommandService customerlistCommandService;

    @Operation(summary = "신규 고객 등록", description = "신규 기업 고객을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/insertCustomer")
    public ResponseEntity<String> create(@RequestBody CustomerlistCommandDTO dto) {
        customerlistCommandService.create(dto);
        return ResponseEntity.ok("고객이 등록되었습니다.");
    }

    @Operation(summary = "고객 정보 수정", description = "기존 고객의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객 ID"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody CustomerlistCommandDTO dto) {
        customerlistCommandService.update(id, dto);
        return ResponseEntity.ok("고객 정보가 수정되었습니다.");
    }

    @Operation(summary = "고객 삭제", description = "고객 정보를 삭제합니다 (논리 삭제).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객 ID"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        customerlistCommandService.delete(id);
        return ResponseEntity.ok("고객이 삭제되었습니다.");
    }

    @Operation(summary = "고객 복구", description = "삭제된 고객 정보를 복구합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "복구 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객 ID"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/restoreCustomer/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        customerlistCommandService.restore(id);
        return ResponseEntity.ok("고객이 복구되었습니다.");
    }
}