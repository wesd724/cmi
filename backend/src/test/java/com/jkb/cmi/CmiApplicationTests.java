package com.jkb.cmi;

import com.jkb.cmi.entity.*;
import com.jkb.cmi.entity.type.Orders;
import com.jkb.cmi.entity.type.Status;
import com.jkb.cmi.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CmiApplicationTests {

	@Autowired
	EntityManager em;
	@Autowired
	CashAssetRepository cashAssetRepository;
	@Autowired
	CurrencyRepository currencyRepository;
	@Autowired
	CurrencyAssetRepository currencyAssetRepository;
	@Autowired
	TradeHistoryRepository tradeHistoryRepository;
	@Autowired
	UserRepository userRepository;

	@Test
	@Transactional
	void Test1() {
		User user = User.builder().username("test").password("123").build();
		userRepository.save(user);

		CashAsset cashAsset = CashAsset.builder().balance(100000L).user(user).build();
		cashAssetRepository.save(cashAsset);

		Currency currency = Currency.builder().market("BTC").name("비트코인").build();
		currencyRepository.save(currency);

		TradeHistory tradeHistory1 = TradeHistory.builder()
				.user(user).currency(currency)
				.orders(Orders.BUY)
				.amount(3.312).price(1508d)
				.status(Status.COMPLETE).build();
		TradeHistory tradeHistory2 = TradeHistory.builder()
				.user(user).currency(currency)
				.orders(Orders.BUY)
				.amount(5d).price(50d)
				.status(Status.COMPLETE).build();
		TradeHistory tradeHistory3 = TradeHistory.builder()
				.user(user).currency(currency)
				.orders(Orders.BUY)
				.amount(250.56203d).price(1500d)
				.status(Status.COMPLETE).build();
		tradeHistoryRepository.saveAll(List.of(tradeHistory1, tradeHistory2, tradeHistory3));

		CurrencyAsset currencyAsset = CurrencyAsset.builder()
				.user(user).currency(currency)
				.amount(5000d).averageCurrencyBuyPrice(270.19d).buyPrice(350002L)
				.build();
		currencyAssetRepository.save(currencyAsset);
	}

	@Test
	@Transactional
	void Test2() {

	}

}
