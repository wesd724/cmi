package com.jkb.cmi.repository;

import com.jkb.cmi.entity.CurrencyAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CurrencyAssetRepository extends JpaRepository<CurrencyAsset, Long> {
    @Query("select c from CurrencyAsset c join fetch c.currency where c.user.username = :username")
    List<CurrencyAsset> getByUser_Username(@Param("username") String username);
    Optional<CurrencyAsset> getByUser_IdAndCurrency_Id(Long userId, Long currencyId);
    Optional<CurrencyAsset> findByUser_UsernameAndCurrency_market(String username, String market);
}
