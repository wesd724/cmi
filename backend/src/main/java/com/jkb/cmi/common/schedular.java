package com.jkb.cmi.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class schedular {

    //@Scheduled(cron = "*/10 * * * * *")
    public void run() {
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt);
    }
}
