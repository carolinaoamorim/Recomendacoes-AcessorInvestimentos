package com.acessorinvestimentos.recomendacoes.controller;

import com.acessorinvestimentos.recomendacoes.dto.RecommendationResponseDto;
import com.acessorinvestimentos.recomendacoes.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/{userId}/generate")
    public List<RecommendationResponseDto> generateRecommendations(@PathVariable String userId) {
        return recommendationService.generateRecommendations(userId);
    }

    @GetMapping("/{userId}")
    public List<RecommendationResponseDto> getRecommendationsByUserId(@PathVariable String userId) {
        return recommendationService.getRecommendationsByUserId(userId);
    }
}
