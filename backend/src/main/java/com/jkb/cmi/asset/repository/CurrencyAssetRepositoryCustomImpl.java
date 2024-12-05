package com.jkb.cmi.asset.repository;

import com.jkb.cmi.external.dto.TickerAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CurrencyAssetRepositoryCustomImpl implements CurrencyAssetRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void saveInitialCurrencyAsset(List<TickerAPIResponse> tickerApiResponses, Long userId) {
        String sql = "INSERT INTO currency_asset " +
                "(user_id, currency_id, amount, buy_price, average_currency_buy_price) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                tickerApiResponses,
                tickerApiResponses.size(),
                (ps, apiResponse) -> {
                    ps.setLong(1, userId);
                    ps.setLong(2, tickerApiResponses.indexOf(apiResponse) + 1);
                    ps.setDouble(3, 100000000 / apiResponse.getTrade_price());
                    ps.setDouble(4, 100000000);
                    ps.setDouble(5, apiResponse.getTrade_price());
                }
        );
    }
}
