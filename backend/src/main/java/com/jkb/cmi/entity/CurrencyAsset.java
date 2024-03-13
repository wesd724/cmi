package com.jkb.cmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "currency_asset")
public class CurrencyAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="currency_id")
    private Currency currency;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double averageCurrencyBuyPrice;

    @Column(nullable = false)
    private Double buyPrice;

    @Builder
    public CurrencyAsset(
            User user,
            Currency currency,
            Double amount,
            Double averageCurrencyBuyPrice,
            Double buyPrice
    ) {
        this.user = user;
        this.currency = currency;
        this.amount = amount;
        this.averageCurrencyBuyPrice = averageCurrencyBuyPrice;
        this.buyPrice = buyPrice;
    }
}
