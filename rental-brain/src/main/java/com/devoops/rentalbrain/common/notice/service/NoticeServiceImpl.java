package com.devoops.rentalbrain.common.notice.service;

import com.devoops.rentalbrain.common.notice.entity.Notification;
import com.devoops.rentalbrain.common.notice.entity.NotificationReceiver;
import com.devoops.rentalbrain.common.notice.repository.NotificationReceiverRepository;
import com.devoops.rentalbrain.common.notice.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {
    private final NotificationRepository notificationRepository;
    private final NotificationReceiverRepository notificationReceiverRepository;

    public NoticeServiceImpl(NotificationRepository notificationRepository,
                             NotificationReceiverRepository notificationReceiverRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationReceiverRepository = notificationReceiverRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void noticeCreate(String noticeType, Long empId){
        log.info("알림 생성 - {}", empId);
        Notification notification = (Notification) notificationRepository.findByType(noticeType)
                .orElseThrow(() ->
                        new IllegalStateException("알림 기준 없음: " + noticeType)
                );
        log.info(notification.toString());
        notificationReceiverRepository.save(
                NotificationReceiver.create(
                        notification.getId(),
                        empId
                )
        );
    }
}

