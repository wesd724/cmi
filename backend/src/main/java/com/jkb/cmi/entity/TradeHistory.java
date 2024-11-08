package com.jkb.cmi.entity;

import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.entity.type.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trade_history")
public class TradeHistory {
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

    @Column(columnDefinition = "decimal(25, 8)", nullable = false)
    private Double amount;

    @Column(columnDefinition = "decimal(25, 8)", nullable = false)
    private Double price;

    @Column(columnDefinition = "datetime(3)")
    private LocalDateTime orderDate;

    @Column(columnDefinition = "datetime")
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
            Status status,
            LocalDateTime orderDate,
            LocalDateTime completeDate
    ) {
        this.user = user;
        this.currency = currency;
        this.orders = orders;
        this.amount = amount;
        this.price = price;
        this.status = status;
        this.orderDate = orderDate;
        this.completeDate = completeDate;
    }

    public void complete() {
        this.completeDate = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

}
