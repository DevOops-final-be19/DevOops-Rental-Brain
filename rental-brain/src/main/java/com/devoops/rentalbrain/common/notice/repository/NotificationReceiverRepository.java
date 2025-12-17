package com.devoops.rentalbrain.common.notice.repository;

import com.devoops.rentalbrain.common.notice.entity.NotificationReceiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationReceiverRepository extends JpaRepository<NotificationReceiver,Long> {
}
