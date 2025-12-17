package com.devoops.rentalbrain.common.notice.command.controller;

import com.devoops.rentalbrain.common.notice.command.service.NoticeCommandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
public class NoticeCommandController {
    private final NoticeCommandService noticeCommandService;

    public NoticeCommandController(NoticeCommandService noticeCommandService) {
        this.noticeCommandService = noticeCommandService;
    }


}
