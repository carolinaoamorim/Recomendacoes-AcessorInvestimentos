package com.acessorinvestimentos.recomendacoes.strategy;

import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationStrategyFactory {

    private final List<RecommendationStrategy> strategies;

    public RecommendationStrategy getStrategy(InvestorType investorType) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(investorType))
                .findFirst()
                .orElseThrow(() -> new RecomendacaoException("Estrategia de recomendacao nao encontrada"));
    }
}
