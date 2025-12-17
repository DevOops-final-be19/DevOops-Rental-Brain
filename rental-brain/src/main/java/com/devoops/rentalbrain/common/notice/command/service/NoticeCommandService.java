package com.devoops.rentalbrain.common.notice.command.service;

public interface NoticeCommandService {
    void noticeCreate(String noticeType, Long empId);
}
