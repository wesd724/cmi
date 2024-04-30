package com.jkb.cmi.repository;

import com.jkb.cmi.entity.Notification;
import com.jkb.cmi.entity.TradeHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jkb.cmi.entity.QNotification.notification;
import static com.jkb.cmi.entity.QTradeHistory.tradeHistory;
import static com.jkb.cmi.entity.QCurrency.currency;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveAllNotification(List<TradeHistory> tradeHistories) {
        String sql = "INSERT INTO notification (user_id, currency_id, trade_history_id, is_read) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                tradeHistories,
                tradeHistories.size(),
                (ps, tradeHistory) -> {
                    ps.setLong(1, tradeHistory.getUser().getId());
                    ps.setLong(2, tradeHistory.getCurrency().getId());
                    ps.setLong(3, tradeHistory.getId());
                    ps.setInt(4, 0);
                }
        );
    }

    @Override
    public List<Notification> findNotificationByUsername(String username) {
        return queryFactory.selectFrom(notification)
                .join(notification.currency, currency)
                .fetchJoin()
                .join(notification.tradeHistory, tradeHistory)
                .fetchJoin()
                .where(notification.user.username.eq(username),
                        notification.isRead.eq(false))
                .fetch();
    }

}
