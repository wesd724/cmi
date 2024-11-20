package com.jkb.cmi.repository;

import com.jkb.cmi.entity.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long>, OrderBookRepositoryCustom {
    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderBook o where o.currency.id = :id")
    List<OrderBook> getByCurrency_Id(@Param("id") Long currencyId);

    List<OrderBook> getByUser_Id(Long userId);

    @Query("select o from OrderBook o join fetch o.currency where o.user.username = :username order by o.id")
    List<OrderBook> getActiveOrderByUsername(@Param("username") String username);

    @Modifying(clearAutomatically = true)
    @Query("delete from OrderBook o where o.id = :id")
    int customDeleteById(@Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from order_book" +
            " where user_id = 2" +
            " and TIMESTAMPDIFF(MINUTE, created_date, NOW()) >= :minute", nativeQuery = true)
    void deleteExpiredVirtualOrder(@Param("minute") int minute);
}
