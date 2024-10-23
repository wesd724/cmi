package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.NotificationResponse;
import com.jkb.cmi.entity.TradeHistory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jkb.cmi.entity.QCurrency.currency;
import static com.jkb.cmi.entity.QNotification.notification;
import static com.jkb.cmi.entity.QTradeHistory.tradeHistory;

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
    public List<NotificationResponse> findNotificationByUsername(String username) {
        return queryFactory.select(
                        Projections.fields(
                                NotificationResponse.class,
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
