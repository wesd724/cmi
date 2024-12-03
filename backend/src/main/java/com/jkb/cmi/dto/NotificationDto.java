package com.jkb.cmi.dto;

import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.entity.type.Status;
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
