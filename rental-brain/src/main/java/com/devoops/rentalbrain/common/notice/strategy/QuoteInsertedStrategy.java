package com.devoops.rentalbrain.common.notice.strategy;

import com.devoops.rentalbrain.common.notice.strategy.event.QuoteInsertedEvent;
import com.devoops.rentalbrain.common.notice.mapper.EmployeePositionMapper;
import com.devoops.rentalbrain.common.notice.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class QuoteInsertedStrategy {
    private final NoticeService noticeService;
    private final EmployeePositionMapper employeePositionMapper;

    public QuoteInsertedStrategy(NoticeService noticeService,
                                 EmployeePositionMapper employeePositionMapper) {
        this.noticeService = noticeService;
        this.employeePositionMapper = employeePositionMapper;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(QuoteInsertedEvent quoteInsertedEvent){
        log.info("QuoteInsertedStrategy EventListener 호출");
        employeePositionMapper.getEmployeeIds(quoteInsertedEvent.positionId().positionNum()).forEach(empId -> {
            log.info(empId.toString());
            noticeService.noticeCreate(
                    "QUOTE_INSERT",
                    empId
            );
        });
    }
}
