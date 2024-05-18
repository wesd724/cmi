package com.jkb.cmi.repository;

import com.jkb.cmi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    @Modifying(clearAutomatically = true)
    @Query("update Notification n set n.isRead = true where n.user.id = :id")
    void readNotification(@Param("id") Long id);
}
