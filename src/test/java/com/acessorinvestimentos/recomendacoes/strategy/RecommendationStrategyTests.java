package com.acessorinvestimentos.recomendacoes.strategy;

import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.entity.Recommendation;
import com.acessorinvestimentos.recomendacoes.entity.RiskLevel;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationStrategyTests {

    @Test
    void conservativeStrategyShouldOnlyRecommendActiveLowRiskAssets() {
        ConservativeRecommendationStrategy strategy = new ConservativeRecommendationStrategy();
        List<Recommendation> recommendations = strategy.recommend(
                profile(InvestorType.CONSERVATIVE),
                List.of(asset(RiskLevel.LOW, true), asset(RiskLevel.MEDIUM, true), asset(RiskLevel.LOW, false))
        );

        assertEquals(1, recommendations.size());
        assertEquals(95, recommendations.get(0).getCompatibilityScore());
        assertEquals(InvestorType.CONSERVATIVE, recommendations.get(0).getInvestorType());
        assertTrue(strategy.supports(InvestorType.CONSERVATIVE));
        assertFalse(strategy.supports(InvestorType.MODERATE));
    }

    @Test
    void moderateStrategyShouldScoreLowAndMediumRiskAssets() {
        ModerateRecommendationStrategy strategy = new ModerateRecommendationStrategy();
        List<Recommendation> recommendations = strategy.recommend(
                profile(InvestorType.MODERATE),
                List.of(
                        asset(RiskLevel.LOW, true),
                        asset(RiskLevel.MEDIUM, true),
                        asset(RiskLevel.HIGH, true),
                        asset(RiskLevel.LOW, false)
                )
        );

        assertEquals(List.of(90, 80), recommendations.stream().map(Recommendation::getCompatibilityScore).toList());
        assertTrue(strategy.supports(InvestorType.MODERATE));
        assertFalse(strategy.supports(InvestorType.AGGRESSIVE));
    }

    @Test
    void aggressiveStrategyShouldScoreEveryActiveRiskLevel() {
        AggressiveRecommendationStrategy strategy = new AggressiveRecommendationStrategy();
        List<Recommendation> recommendations = strategy.recommend(
                profile(InvestorType.AGGRESSIVE),
                List.of(
                        asset(RiskLevel.LOW, true),
                        asset(RiskLevel.MEDIUM, true),
                        asset(RiskLevel.HIGH, true),
                        asset(RiskLevel.HIGH, false)
                )
        );

        assertEquals(List.of(70, 85, 95), recommendations.stream().map(Recommendation::getCompatibilityScore).toList());
        assertTrue(strategy.supports(InvestorType.AGGRESSIVE));
        assertFalse(strategy.supports(InvestorType.CONSERVATIVE));
    }

    @Test
    void factoryShouldReturnSupportedStrategyAndRejectUnsupportedType() {
        RecommendationStrategy conservative = new ConservativeRecommendationStrategy();
        RecommendationStrategy moderate = new ModerateRecommendationStrategy();
        RecommendationStrategyFactory factory = new RecommendationStrategyFactory(List.of(conservative, moderate));

        assertSame(conservative, factory.getStrategy(InvestorType.CONSERVATIVE));
        assertSame(moderate, factory.getStrategy(InvestorType.MODERATE));
        assertThrows(RecomendacaoException.class, () -> factory.getStrategy(InvestorType.AGGRESSIVE));
        assertThrows(RecomendacaoException.class, () -> factory.getStrategy(null));
    }

    private ProfileResponseDto profile(InvestorType investorType) {
        return new ProfileResponseDto("auth0|123", investorType);
    }

    private Asset asset(RiskLevel riskLevel, boolean active) {
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID());
        asset.setRiskLevel(riskLevel);
        asset.setActive(active);
        return asset;
    }
}
