package com.devoops.rentalbrain.approval.command.service;

public interface ApprovalCommandService {
    void approve(Long approvalId);
    void reject(Long approvalId, String rejectReason);
}
