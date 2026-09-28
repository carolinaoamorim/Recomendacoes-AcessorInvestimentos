package com.acessorinvestimentos.recomendacoes.strategy;

import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.entity.Recommendation;

import java.util.List;

public interface RecommendationStrategy {
    List<Recommendation> recommend(ProfileResponseDto profile, List<Asset> assets);

    boolean supports(InvestorType investorType);
}
