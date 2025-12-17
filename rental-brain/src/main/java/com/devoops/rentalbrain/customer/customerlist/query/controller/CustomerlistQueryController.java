package com.devoops.rentalbrain.customer.customerlist.query.controller;

import com.devoops.rentalbrain.common.pagination.PageResponseDTO;
import com.devoops.rentalbrain.customer.common.CustomerDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerDetailResponseDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerKpiDTO;
import com.devoops.rentalbrain.customer.customerlist.query.dto.CustomerlistSearchDTO;
import com.devoops.rentalbrain.customer.customerlist.query.service.CustomerlistQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "고객 관리(Query)", description = "고객 목록, 상세, KPI 조회 API")
public class CustomerlistQueryController {

    private final CustomerlistQueryService customerlistQueryService;

    @Operation(summary = "고객 목록 조회", description = "검색어, 세그먼트 필터, 페이징을 적용하여 고객 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<CustomerDTO>> list(@ModelAttribute CustomerlistSearchDTO searchDTO) {
        return ResponseEntity.ok(customerlistQueryService.findAll(searchDTO));
    }

    @Operation(summary = "고객 상세 조회", description = "고객 정보 및 관련 내역(문의, 견적, 계약, AS 등)을 모두 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 고객"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailResponseDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(customerlistQueryService.findById(id));
    }

    @Operation(summary = "고객 KPI 조회", description = "대시보드용 KPI 데이터(총 고객, VIP 비율, 이탈 위험 등)를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/kpi")
    public ResponseEntity<CustomerKpiDTO> getKpi() {
        return ResponseEntity.ok(customerlistQueryService.getKpi());
    }
}