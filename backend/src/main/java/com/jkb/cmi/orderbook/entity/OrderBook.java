package com.jkb.cmi.orderbook.entity;

import com.jkb.cmi.common.entity.BaseEntity;
import com.jkb.cmi.currency.entity.Currency;
import com.jkb.cmi.common.enums.Orders;
import com.jkb.cmi.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_book")
public class OrderBook extends BaseEntity {
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
    private Double originalAmount;

    @Column(columnDefinition = "decimal(25, 8)", nullable = false)
    private Double activeAmount;

    @Column(columnDefinition = "decimal(25, 8)", nullable = false)
    private Double price;

    @Builder
    public OrderBook(
            User user,
            Currency currency,
            Orders orders,
            Double originalAmount,
            Double price
    ) {
        this.user = user;
        this.currency = currency;
        this.orders = orders;
        this.originalAmount = originalAmount;
        this.activeAmount = originalAmount;
        this.price = price;
    }

    public void changeActiveAmount(double amount) {
        this.activeAmount -= amount;
    }
}
