package com.jkb.cmi.repository;

import com.jkb.cmi.entity.CurrencyAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyAssetRepository extends JpaRepository<CurrencyAsset, Long> {
    Optional<CurrencyAsset> getByUser_IdAndCurrency_Id(Long userId, Long currencyId);
}
