package com.acessorinvestimentos.recomendacoes.event;

import com.acessorinvestimentos.recomendacoes.dto.ProfileUpdatedEventDto;
import com.acessorinvestimentos.recomendacoes.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileUpdatedEventConsumer {

    private final RecommendationService recommendationService;

    public void consume(ProfileUpdatedEventDto event) {
        if (event != null && event.getUserId() != null && !event.getUserId().isBlank()) {
            recommendationService.generateRecommendations(event.getUserId());
        }
    }
}
