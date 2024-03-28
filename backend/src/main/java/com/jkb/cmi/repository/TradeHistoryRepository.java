package com.jkb.cmi.repository;

import com.jkb.cmi.entity.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {
    @Query("select t from TradeHistory t join fetch t.currency where t.user.username = :username order by t.id asc")
    List<TradeHistory> findByUsername(@Param("username") String username);
    List<TradeHistory> findByPriceLessThanEqualAndCompleteFalse(Double price);
    List<TradeHistory> findByCompleteTrueAndCompleteDateNull();
}
