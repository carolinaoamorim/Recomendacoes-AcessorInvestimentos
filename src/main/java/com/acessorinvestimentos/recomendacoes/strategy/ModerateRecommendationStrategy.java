package com.acessorinvestimentos.recomendacoes.strategy;

import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.entity.Recommendation;
import com.acessorinvestimentos.recomendacoes.entity.RiskLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModerateRecommendationStrategy implements RecommendationStrategy {

    @Override
    public List<Recommendation> recommend(ProfileResponseDto profile, List<Asset> assets) {
        return assets.stream()
                .filter(asset -> Boolean.TRUE.equals(asset.getActive()))
                .filter(asset -> asset.getRiskLevel() == RiskLevel.LOW || asset.getRiskLevel() == RiskLevel.MEDIUM)
                .map(asset -> Recommendation.fromAsset(
                        profile.getUserId(),
                        InvestorType.MODERATE,
                        asset,
                        compatibilityScore(asset.getRiskLevel()),
                        "Ativo compativel com perfil moderado"
                ))
                .toList();
    }

    @Override
    public boolean supports(InvestorType investorType) {
        return investorType == InvestorType.MODERATE;
    }

    private Integer compatibilityScore(RiskLevel riskLevel) {
        return riskLevel == RiskLevel.LOW ? 90 : 80;
    }
}
