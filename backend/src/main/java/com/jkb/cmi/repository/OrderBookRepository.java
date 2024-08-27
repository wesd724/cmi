package com.jkb.cmi.repository;

import com.jkb.cmi.entity.OrderBook;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OrderBook> getByCurrency_Id(Long Currency_id);
}
