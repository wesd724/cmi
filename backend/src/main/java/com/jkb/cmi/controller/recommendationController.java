package com.jkb.cmi.controller;

import com.jkb.cmi.dto.response.RecommendationResponse;
import com.jkb.cmi.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class recommendationController {

    private final RecommendationService recommendationService;
    @GetMapping
    public ResponseEntity<List<RecommendationResponse>> getRecommendation() {
        return ResponseEntity.ok(recommendationService.getRecommendation());
    }
}
