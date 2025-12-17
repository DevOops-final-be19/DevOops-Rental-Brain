package com.devoops.rentalbrain.common.notice.application.strategy;

import com.devoops.rentalbrain.common.notice.application.strategy.event.QuoteInsertedEvent;
import com.devoops.rentalbrain.common.notice.command.service.NoticeCommandService;
import com.devoops.rentalbrain.common.notice.query.mapper.NoticeQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class QuoteInsertedStrategy {
    private final NoticeCommandService noticeCommandService;
    private final NoticeQueryMapper noticeQueryMapper;

    public QuoteInsertedStrategy(NoticeCommandService noticeCommandService,
                                 NoticeQueryMapper noticeQueryMapper) {
        this.noticeCommandService = noticeCommandService;
        this.noticeQueryMapper = noticeQueryMapper;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(QuoteInsertedEvent quoteInsertedEvent){
        log.info("QuoteInsertedStrategy EventListener 호출");
        noticeQueryMapper.getEmployeeIds(quoteInsertedEvent.positionId().positionNum()).forEach(empId -> {
            log.info(empId.toString());
            noticeCommandService.noticeCreate(
                    "QUOTE_INSERT",
                    empId
            );
        });
    }
}
