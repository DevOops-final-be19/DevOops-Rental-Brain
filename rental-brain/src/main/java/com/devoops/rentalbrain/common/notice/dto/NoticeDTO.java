package com.devoops.rentalbrain.common.notice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NoticeDTO {
    private Long id;
    private String type;
    private String title;
    private String message;
    private String linkUrl;
}
