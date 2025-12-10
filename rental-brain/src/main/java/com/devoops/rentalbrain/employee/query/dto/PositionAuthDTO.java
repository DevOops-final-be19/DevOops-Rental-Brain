package com.devoops.rentalbrain.employee.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PositionAuthDTO {
    private Long emp_position_auth_id;
    private Long ep_position_id;
    private Long ep_auth_id;
}
