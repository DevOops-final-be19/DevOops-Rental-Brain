package com.devoops.rentalbrain.approval.command.Controller;

import com.devoops.rentalbrain.approval.command.dto.ApprovalRejectRequest;
import com.devoops.rentalbrain.approval.command.service.ApprovalCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approval")
public class ApprovalCommandController {
    private final ApprovalCommandService approvalCommandService;

    @Autowired
    public ApprovalCommandController(ApprovalCommandService approvalCommandService) {
        this.approvalCommandService = approvalCommandService;
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long id
    ) {
        approvalCommandService.approve(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Long id,
            @RequestBody ApprovalRejectRequest request
    ) {
        approvalCommandService.reject(id, request.getRejectReason());
        return ResponseEntity.noContent().build();
    }
}
