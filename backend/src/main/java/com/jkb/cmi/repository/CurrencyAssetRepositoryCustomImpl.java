package com.jkb.cmi.repository;

import com.jkb.cmi.dto.response.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CurrencyAssetRepositoryCustomImpl implements CurrencyAssetRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void saveInitialCurrencyAsset(List<APIResponse> apiResponses, Long userId) {
        String sql = "INSERT INTO currency_asset " +
                "(user_id, currency_id, amount, buy_price, average_currency_buy_price) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                apiResponses,
                apiResponses.size(),
                (ps, apiResponse) -> {
                    ps.setLong(1, userId);
                    ps.setLong(2, apiResponses.indexOf(apiResponse) + 1);
                    ps.setDouble(3, 100d);
                    ps.setDouble(4, apiResponse.getTrade_price() * 100);
                    ps.setDouble(5, apiResponse.getTrade_price());
                }
        );
    }
}
