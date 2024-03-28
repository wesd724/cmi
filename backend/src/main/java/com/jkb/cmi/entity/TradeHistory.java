package com.jkb.cmi.entity;

import com.jkb.cmi.entity.type.Orders;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
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

    @ColumnDefault("0")
    private boolean complete;

    @Builder
    public TradeHistory(
            User user,
            Currency currency,
            Orders orders,
            Double amount,
            Double price,
            boolean complete
    ) {
        this.user = user;
        this.currency = currency;
        this.orders = orders;
        this.amount = amount;
        this.price = price;
        this.complete = complete;
    }

    public void complete() {
        this.completeDate = LocalDateTime.now();
    }

}
