package com.jkb.cmi.notification.dto;

import com.jkb.cmi.common.enums.Orders;
import com.jkb.cmi.tradehistory.entity.type.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private String currencyName;
    private Orders orders;
    private Double amount;
    private Double price;
    private Status status;
    private LocalDateTime completeDate;
}
