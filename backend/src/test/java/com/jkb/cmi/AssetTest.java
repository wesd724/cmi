package com.jkb.cmi;

import com.jkb.cmi.common.Scheduler;
import com.jkb.cmi.service.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssetTest {
    @Autowired
    AssetService assetService;

    @Autowired
    Scheduler scheduler;

    @Test
    void test1() {
        scheduler.run();
    }
}
