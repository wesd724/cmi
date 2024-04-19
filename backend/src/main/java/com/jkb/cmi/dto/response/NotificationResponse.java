package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.Notification;
import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.type.Orders;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponse {
    private String currencyName;
    private Orders orders;
    private Double amount;
    private Double price;
    private LocalDateTime completeDate;

    public static NotificationResponse from(Notification notification) {
        TradeHistory tradeHistory = notification.getTradeHistory();
        return new NotificationResponse(
                tradeHistory.getCurrency().getName(),
                tradeHistory.getOrders(),
                tradeHistory.getAmount(),
                tradeHistory.getPrice(),
                tradeHistory.getCompleteDate()
        );
    }

    public static List<NotificationResponse> tolist(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
