package com.jkb.cmi.entity;

import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.entity.type.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trade_history")
public class TradeHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Currency_id")
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Orders orders;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double price;

    private LocalDateTime completeDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public TradeHistory(
            User user,
            Currency currency,
            Orders orders,
            Double amount,
            Double price,
            Status status
    ) {
        this.user = user;
        this.currency = currency;
        this.orders = orders;
        this.amount = amount;
        this.price = price;
        this.status = status;
    }

    public void complete() {
        this.completeDate = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

}
