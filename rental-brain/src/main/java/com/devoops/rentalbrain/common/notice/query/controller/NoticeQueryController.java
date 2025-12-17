package com.devoops.rentalbrain.common.notice.query.controller;

import com.devoops.rentalbrain.common.notice.query.dto.NoticeReceiveDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeQueryController {
    @GetMapping("/list/{empId}")
    public ResponseEntity<List<NoticeReceiveDTO>> getNewNoticeList(@PathVariable Long empId) {
        List<NoticeReceiveDTO> noticeReceiveDTO = null;
        return ResponseEntity.ok().body(noticeReceiveDTO);
    }
}
