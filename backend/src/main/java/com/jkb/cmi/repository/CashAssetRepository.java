package com.jkb.cmi.repository;

import com.jkb.cmi.entity.CashAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashAssetRepository extends JpaRepository<CashAsset, Long> {
    CashAsset getByUser_Username(String username);
    CashAsset getByUser_Id(Long id);
}
