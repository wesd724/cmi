package com.jkb.cmi.repository;

import com.jkb.cmi.entity.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {
    List<TradeHistory> findByUser_id(Long id);
    List<TradeHistory> findByPriceLessThanEqualAndCompleteFalse(Double price);

    List<TradeHistory> findByCompleteTrueAndCompleteDateNull();
}
