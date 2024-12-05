package com.jkb.cmi.orderbook.repository;

import com.jkb.cmi.orderbook.dto.OrderBookDto;
import com.jkb.cmi.orderbook.entity.OrderBook;
import com.jkb.cmi.common.enums.Orders;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jkb.cmi.orderbook.entity.QOrderBook.orderBook;

@Repository
@RequiredArgsConstructor
public class OrderBookRepositoryCustomImpl implements OrderBookRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderBook> findByPriceForUpdate(Long currencyId, Double price, Orders orders) {
        return queryFactory.selectFrom(orderBook)
                .where(orderBook.currency.id.eq(currencyId),
                        eqOrders(orders),
                        rangePrice(price, orders))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();
    }

    private BooleanExpression rangePrice(double price, Orders orders) {
        return orders == Orders.BUY ?
                orderBook.price.loe(price) :
                orderBook.price.goe(price);
    }

    private BooleanExpression eqOrders(Orders orders) {
        return orders == Orders.BUY ?
                orderBook.orders.eq(Orders.SELL) :
                orderBook.orders.eq(Orders.BUY);
    }

    @Override
    public List<OrderBookDto> getByCurrencyId(Long currencyId) {
        return queryFactory.select(
                Projections.fields(
                        OrderBookDto.class,
                        orderBook.orders,
                        orderBook.activeAmount,
                        orderBook.price,
                        orderBook.createdDate
                )
        )
                .from(orderBook)
                .where(orderBook.currency.id.eq(currencyId))
                .fetch();
    }
}
