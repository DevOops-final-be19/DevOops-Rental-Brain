package com.devoops.rentalbrain.common.notice.application.strategy;

import com.devoops.rentalbrain.common.notice.application.strategy.event.ContractApprovedEvent;
import com.devoops.rentalbrain.common.notice.command.service.NoticeCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ContractApprovedStrategy {
    private final NoticeCommandService noticeCommandService;

    public ContractApprovedStrategy(NoticeCommandService noticeCommandService) {
        this.noticeCommandService = noticeCommandService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ContractApprovedEvent contractApprovedEvent){
        log.info("ContractApprovedStrategy EventListener 호출");
        noticeCommandService.noticeCreate(
                "APPROVAL",
                contractApprovedEvent.EmpId()
        );
    }
}
