package com.devoops.rentalbrain.customer.customerlist.query.dto;

import com.devoops.rentalbrain.common.pagination.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerlistSearchDTO extends Criteria {
    // pageNum, amount는 부모 클래스(Criteria)에 존재함
    private String keyword;        // 통합 검색어 (이름, 담당자, 연락처 등)
    private List<String> segments; // 세그먼트 필터 (다중 선택)
    private String status;         // 계정 상태
}