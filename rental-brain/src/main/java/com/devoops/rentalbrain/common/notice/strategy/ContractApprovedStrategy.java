package com.devoops.rentalbrain.common.notice.strategy;

import com.devoops.rentalbrain.common.notice.strategy.event.ContractApprovedEvent;
import com.devoops.rentalbrain.common.notice.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ContractApprovedStrategy {
    private final NoticeService noticeService;

    public ContractApprovedStrategy(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ContractApprovedEvent contractApprovedEvent){
        log.info("ContractApprovedStrategy EventListener 호출");
        noticeService.noticeCreate(
                "APPROVAL",
                contractApprovedEvent.EmpId()
        );
    }
}
