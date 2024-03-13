package com.jkb.cmi.repository;

import com.jkb.cmi.entity.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {
}
