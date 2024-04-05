package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.APIResponse;
import com.jkb.cmi.entity.type.Orders;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.jkb.cmi.entity.QTradeHistory.tradeHistory;

@Repository
@RequiredArgsConstructor
public class TradeHistoryRepositoryCustomImpl implements TradeHistoryRepositoryCustom {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public void tradeCompleteProcessing(List<APIResponse> dtoList, Orders orders) {
        AtomicLong index = new AtomicLong(1);
        BooleanBuilder builder = new BooleanBuilder();

        if(orders == Orders.BUY) {
            dtoList.forEach(dto ->
                    builder.or(eqCurrencyId(index.getAndIncrement())
                            .and(goePrice(dto.getTrade_price()))
                    )
            );
        } else {
            dtoList.forEach(dto ->
                    builder.or(eqCurrencyId(index.getAndIncrement())
                            .and(loePrice(dto.getTrade_price()))
                    )
            );
        }

        queryFactory.update(tradeHistory)
                .set(tradeHistory.complete, true)
                .where(builder,
                        eqOrders(orders),
                        eqComplete(false))
                .execute();

        em.flush();
        em.clear();
    }

    private BooleanExpression eqCurrencyId(Long currencyId) {
        return tradeHistory.currency.id.eq(currencyId);
    }

    private BooleanExpression goePrice(Double price) {
        return tradeHistory.price.goe(price);
    }

    private BooleanExpression loePrice(Double price) {
        return tradeHistory.price.loe(price);
    }

    private BooleanExpression eqOrders(Orders orders) {
        return tradeHistory.orders.eq(orders);
    }

    private BooleanExpression eqComplete(boolean complete) {
        return tradeHistory.complete.eq(complete);
    }
}
