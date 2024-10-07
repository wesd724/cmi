package com.jkb.cmi;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class NotificationTest {
    @Autowired
    NotificationRepository notificationRepository;

    @Test
    @Transactional(readOnly = true)
    void test1() {
        List<NotificationResponse> notificationResponses = notificationRepository.findNotificationByUsername("test");
        notificationResponses.forEach(System.out::println);
    }

    @Test
    @Transactional
    void test2() {

    }

}
