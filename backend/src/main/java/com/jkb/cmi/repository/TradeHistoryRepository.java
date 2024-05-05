package com.jkb.cmi.repository;

import com.jkb.cmi.entity.TradeHistory;
import com.jkb.cmi.entity.type.Orders;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long>, TradeHistoryRepositoryCustom {
    List<TradeHistory> findByPriceLessThanEqualAndCompleteFalse(Double price);
    List<TradeHistory> findByPriceLessThanEqualAndCompleteDateNull(Double price);
    @Modifying(clearAutomatically = true)
    @Query("update TradeHistory t set t.complete = true where t.currency.id = :currencyId and t.orders = Orders.BUY and t.price >= :price")
    void buyTradeCompleteProcessing(@Param("currencyId") Long currencyId, @Param("price") Double price);
    @Modifying(clearAutomatically = true)
    @Query("update TradeHistory t set t.complete = true where t.currency.id = :currencyId and t.orders = Orders.SELL and t.price <= :price")
    void sellTradeCompleteProcessing(@Param("currencyId") Long currencyId, @Param("price") Double price);

    @Query("select t from TradeHistory t join fetch t.currency where t.user.username = :username order by t.id asc")
    List<TradeHistory> findByUsername(@Param("username") String username);
    List<TradeHistory> findByCompleteTrueAndCompleteDateNull();
    @Query("select t from TradeHistory t join fetch t.currency where t.user.username = :username and t.complete = false")
    List<TradeHistory> findByUsernameAndCompleteFalse(@Param("username") String username);
}
