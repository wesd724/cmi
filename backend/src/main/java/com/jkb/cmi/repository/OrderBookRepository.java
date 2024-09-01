package com.jkb.cmi.repository;

import com.jkb.cmi.entity.OrderBook;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OrderBook> getByCurrency_Id(Long Currency_id);

    @Query("select o from OrderBook o join fetch o.currency where o.user.username = :username")
    List<OrderBook> getActiveOrderByUsername(@Param("username") String username);

    @Modifying(clearAutomatically = true)
    @Query("delete from OrderBook o where o.id = :id")
    int customDeleteById(@Param("id") Long id);
}
