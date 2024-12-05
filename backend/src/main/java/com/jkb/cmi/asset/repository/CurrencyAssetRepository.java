package com.jkb.cmi.asset.repository;

import com.jkb.cmi.asset.entity.CurrencyAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CurrencyAssetRepository extends JpaRepository<CurrencyAsset, Long>, CurrencyAssetRepositoryCustom {
    @Query("select c from CurrencyAsset c join fetch c.currency where c.user.username = :username order by c.amount desc")
    List<CurrencyAsset> getByUser_Username(@Param("username") String username);
    Optional<CurrencyAsset> getByUser_IdAndCurrency_Id(Long userId, Long currencyId);
    Optional<CurrencyAsset> findByUser_UsernameAndCurrency_market(String username, String market);
    @Modifying(clearAutomatically = true)
    @Query("delete from CurrencyAsset c where c.user.id = :userId and c.currency.id = :currencyId")
    void deleteByCurrency(@Param("userId") Long userId, @Param("currencyId") Long currencyId);
}
