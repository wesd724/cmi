package com.jkb.cmi;

import com.jkb.cmi.repository.CurrencyAssetRepository;
import com.jkb.cmi.service.CurrencyAssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CurrencyAssetTest {
    @Autowired
    CurrencyAssetRepository currencyAssetRepository;
    @Autowired
    CurrencyAssetService currencyAssetService;

    @Test
    void test1() {
        currencyAssetService.updateCurrencyAsset();
    }

    @Test
    void test2() {
        currencyAssetRepository.getByUser_Username("test");
    }
}
