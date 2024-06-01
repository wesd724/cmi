package com.jkb.cmi.service;

import com.jkb.cmi.dto.response.RecommendationResponse;
import com.jkb.cmi.entity.Recommendation;
import com.jkb.cmi.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<RecommendationResponse> getRecommendation() {
        List<Recommendation> recommendations = recommendationRepository.findTop5ByOrderByComparedPreviousDayDesc();
        return RecommendationResponse.tolist(recommendations);
    }

    public void updateRecommendation(List<RecommendationResponse> recommendationResponses) {
        recommendationResponses.forEach(recommendationResponse -> {
            recommendationRepository.update(recommendationResponse.getMarket(), recommendationResponse.getComparedPreviousDay());
        });
    }
}
