package com.jkb.cmi.notification.repository;

import com.jkb.cmi.notification.dto.NotificationDto;
import com.jkb.cmi.tradehistory.entity.TradeHistory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jkb.cmi.currency.entity.QCurrency.currency;
import static com.jkb.cmi.tradehistory.entity.QTradeHistory.tradeHistory;
import static com.jkb.cmi.notification.entity.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveAllNotification(List<TradeHistory> tradeHistories) {
        String sql = "INSERT INTO notification (user_id, trade_history_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql,
                tradeHistories,
                tradeHistories.size(),
                (ps, tradeHistory) -> {
                    ps.setLong(1, tradeHistory.getUser().getId());
                    ps.setLong(2, tradeHistory.getId());
                }
        );
    }

    @Override
    public List<NotificationDto> findNotificationByUsername(String username) {
        return queryFactory.select(
                        Projections.fields(
                                NotificationDto.class,
                                notification.id,
                                currency.name.as("currencyName"),
                                tradeHistory.orders,
                                tradeHistory.amount,
                                tradeHistory.price,
                                tradeHistory.status,
                                tradeHistory.completeDate
                        )
                )
                .from(notification)
                .join(notification.tradeHistory, tradeHistory)
                .join(tradeHistory.currency, currency)
                .where(notification.user.username.eq(username))
                .orderBy(notification.id.asc())
                .fetch();
    }

}
