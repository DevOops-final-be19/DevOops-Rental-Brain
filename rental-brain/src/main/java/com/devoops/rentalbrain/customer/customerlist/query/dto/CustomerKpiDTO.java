package com.devoops.rentalbrain.customer.customerlist.query.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerKpiDTO {
    private int totalCount;
    private int vipCount;
    private int riskCount;
    private int blacklistCount;
    private double vipRate; // VIP 비율
}