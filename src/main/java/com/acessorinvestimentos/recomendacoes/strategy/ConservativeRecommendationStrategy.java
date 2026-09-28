package com.acessorinvestimentos.recomendacoes.strategy;

import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.entity.Recommendation;
import com.acessorinvestimentos.recomendacoes.entity.RiskLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConservativeRecommendationStrategy implements RecommendationStrategy {

    @Override
    public List<Recommendation> recommend(ProfileResponseDto profile, List<Asset> assets) {
        return assets.stream()
                .filter(asset -> Boolean.TRUE.equals(asset.getActive()))
                .filter(asset -> asset.getRiskLevel() == RiskLevel.LOW)
                .map(asset -> Recommendation.fromAsset(
                        profile.getUserId(),
                        InvestorType.CONSERVATIVE,
                        asset,
                        95,
                        "Ativo de baixo risco compativel com perfil conservador"
                ))
                .toList();
    }

    @Override
    public boolean supports(InvestorType investorType) {
        return investorType == InvestorType.CONSERVATIVE;
    }
}
