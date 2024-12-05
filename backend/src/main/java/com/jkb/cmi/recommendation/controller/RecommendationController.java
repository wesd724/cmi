package com.jkb.cmi.recommendation.controller;

import com.jkb.cmi.recommendation.dto.RecommendationResponse;
import com.jkb.cmi.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;
    @GetMapping
    public ResponseEntity<List<RecommendationResponse>> getRecommendation() {
        return ResponseEntity.ok(recommendationService.getRecommendation());
    }
}
