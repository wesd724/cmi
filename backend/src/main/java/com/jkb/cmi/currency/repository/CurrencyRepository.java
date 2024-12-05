package com.jkb.cmi.currency.repository;

import com.jkb.cmi.currency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
