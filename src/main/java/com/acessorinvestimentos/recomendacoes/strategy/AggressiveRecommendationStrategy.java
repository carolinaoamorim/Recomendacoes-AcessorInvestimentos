package com.acessorinvestimentos.recomendacoes.strategy;

import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.entity.Recommendation;
import com.acessorinvestimentos.recomendacoes.entity.RiskLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AggressiveRecommendationStrategy implements RecommendationStrategy {

    @Override
    public List<Recommendation> recommend(ProfileResponseDto profile, List<Asset> assets) {
        return assets.stream()
                .filter(asset -> Boolean.TRUE.equals(asset.getActive()))
                .map(asset -> Recommendation.fromAsset(
                        profile.getUserId(),
                        InvestorType.AGGRESSIVE,
                        asset,
                        compatibilityScore(asset.getRiskLevel()),
                        "Ativo compativel com perfil arrojado"
                ))
                .toList();
    }

    @Override
    public boolean supports(InvestorType investorType) {
        return investorType == InvestorType.AGGRESSIVE;
    }

    private Integer compatibilityScore(RiskLevel riskLevel) {
        if (riskLevel == RiskLevel.HIGH) {
            return 95;
        }
        if (riskLevel == RiskLevel.MEDIUM) {
            return 85;
        }
        return 70;
    }
}
