package com.jkb.cmi.repository;

import com.jkb.cmi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    @Override
    @Modifying(clearAutomatically = true)
    @Query("delete from Notification n where n.id = :id")
    void deleteById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from Notification n where n.user.id = :id")
    void checkAllNotification(@Param("id") Long id);


}
