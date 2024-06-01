package com.jkb.cmi.repository;

import com.jkb.cmi.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findTop5ByOrderByComparedPreviousDayDesc();

    @Modifying(clearAutomatically = true)
    @Query("update Recommendation r set r.comparedPreviousDay = :price where r.market = :market")
    void update(@Param("market") String market, @Param("price") Double price);
}
