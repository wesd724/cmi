package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.entity.type.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponse {
    private Long id;
    private String currencyName;
    private Orders orders;
    private Double amount;
    private Double price;
    private Status status;
    private LocalDateTime completeDate;
}
