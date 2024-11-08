package com.jkb.cmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "currency_asset")
public class CurrencyAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="currency_id")
    private Currency currency;

    @Column(columnDefinition = "decimal(25, 8)", nullable = false)
    private Double amount;

    @Column(columnDefinition = "decimal(25, 8)", nullable = false)
    private Double averageCurrencyBuyPrice;

    @Column(nullable = false)
    private Long buyPrice;

    @Builder
    public CurrencyAsset(
            User user,
            Currency currency,
            Double amount,
            Double averageCurrencyBuyPrice,
            Long buyPrice
    ) {
        this.user = user;
        this.currency = currency;
        this.amount = amount;
        this.averageCurrencyBuyPrice = averageCurrencyBuyPrice;
        this.buyPrice = buyPrice;
    }

    public void changeAmount(Double amount) {
        this.amount += amount;
    }

    public void setAverageCurrencyBuyPrice() {
        this.averageCurrencyBuyPrice = this.buyPrice / this.amount;
    }

    public void changeBuyPrice(Long price) {
        this.buyPrice += price;
    }
}
