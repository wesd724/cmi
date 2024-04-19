package com.jkb.cmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "trade_history_id")
    private TradeHistory tradeHistory;

    @Column(nullable = false)
    private boolean isRead;

    @Builder
    public Notification(User user, Currency currency, TradeHistory tradeHistory, boolean isRead) {
        this.user = user;
        this.currency = currency;
        this.tradeHistory = tradeHistory;
        this.isRead = isRead;
    }
}
