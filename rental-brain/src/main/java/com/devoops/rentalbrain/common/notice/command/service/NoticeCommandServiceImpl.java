package com.devoops.rentalbrain.common.notice.command.service;

import com.devoops.rentalbrain.common.notice.application.domain.PositionType;
import com.devoops.rentalbrain.common.notice.application.strategy.event.NotificationEvent;
import com.devoops.rentalbrain.common.notice.command.entity.Notification;
import com.devoops.rentalbrain.common.notice.command.entity.NotificationReceiver;
import com.devoops.rentalbrain.common.notice.command.repository.NotificationReceiverRepository;
import com.devoops.rentalbrain.common.notice.command.repository.NotificationRepository;
import com.devoops.rentalbrain.common.notice.query.mapper.NoticeQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class NoticeCommandServiceImpl implements NoticeCommandService {
    private final NotificationRepository notificationRepository;
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NoticeQueryMapper noticeQueryMapper;

    public NoticeCommandServiceImpl(NotificationRepository notificationRepository,
                             NotificationReceiverRepository notificationReceiverRepository,
                                    NoticeQueryMapper noticeQueryMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationReceiverRepository = notificationReceiverRepository;
        this.noticeQueryMapper = noticeQueryMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void noticeCreate(Notification notification,Long empId){
        Notification getNotice = notificationRepository.save(notification);
        log.info("알림 생성 - {}", getNotice);

        log.info(notification.toString());
        notificationReceiverRepository.save(
                NotificationReceiver.create(
                        notification.getId(),
                        empId
                )
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void noticeAllCreate(Notification notification, PositionType positionId){
        Notification getNotice = notificationRepository.save(notification);
        log.info("알림 생성 - {}", getNotice);

        log.info(getNotice.toString());
        noticeQueryMapper.getEmployeeIds(positionId.positionNum()).forEach(empId -> {
            notificationReceiverRepository.save(
                    NotificationReceiver.create(
                            notification.getId(),
                            empId
                    )
            );
        });

    }
}
