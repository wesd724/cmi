package com.jkb.cmi.dto.response;

import com.jkb.cmi.entity.Recommendation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendationResponse {
    private String market;
    private Double comparedPreviousDay;

    public static List<RecommendationResponse> tolist(List<Recommendation> recommendations) {
        return recommendations.stream()
                .map(recommendation ->
                        new RecommendationResponse(
                                recommendation.getMarket(), recommendation.getComparedPreviousDay())
                ).toList();
    }
}
