package com.devoops.rentalbrain.common.notice.repository;

import com.devoops.rentalbrain.common.notice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Optional<Object> findByType(String type);
}
