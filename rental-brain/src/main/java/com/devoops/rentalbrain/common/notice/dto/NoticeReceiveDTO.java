package com.devoops.rentalbrain.common.notice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NoticeReceiveDTO {
    private Long id;
    private Character isRead;
    private String createAt;
    private String readAt;
    private NoticeDTO notice;
    private Long empId;
}
