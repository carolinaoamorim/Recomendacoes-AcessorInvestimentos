package com.acessorinvestimentos.recomendacoes.service;

import com.acessorinvestimentos.recomendacoes.client.InvestorProfileClient;
import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.dto.RecommendationResponseDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.Recommendation;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import com.acessorinvestimentos.recomendacoes.repository.AssetRepository;
import com.acessorinvestimentos.recomendacoes.repository.RecommendationRepository;
import com.acessorinvestimentos.recomendacoes.strategy.RecommendationStrategy;
import com.acessorinvestimentos.recomendacoes.strategy.RecommendationStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final AssetRepository assetRepository;
    private final InvestorProfileClient investorProfileClient;
    private final RecommendationStrategyFactory strategyFactory;

    @Transactional
    public List<RecommendationResponseDto> generateRecommendations(String userId) {
        validateUserId(userId);

        ProfileResponseDto profile = investorProfileClient.getInvestorProfile(userId);
        List<Asset> assets = assetRepository.findByActiveTrue();

        RecommendationStrategy strategy = strategyFactory.getStrategy(profile.getInvestorType());
        List<Recommendation> recommendations = strategy.recommend(profile, assets);

        recommendationRepository.deleteByUserId(userId);
        List<Recommendation> savedRecommendations = recommendationRepository.saveAll(recommendations);

        Map<UUID, Asset> assetsById = assets.stream()
                .collect(Collectors.toMap(Asset::getId, Function.identity()));

        return savedRecommendations.stream()
                .map(recommendation -> RecommendationResponseDto.fromEntity(
                        recommendation,
                        assetsById.get(recommendation.getAssetId())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponseDto> getRecommendationsByUserId(String userId) {
        validateUserId(userId);

        List<Recommendation> recommendations = recommendationRepository.findByUserId(userId);
        List<UUID> assetIds = recommendations.stream()
                .map(Recommendation::getAssetId)
                .distinct()
                .toList();

        Map<UUID, Asset> assetsById = assetRepository.findAllById(assetIds).stream()
                .collect(Collectors.toMap(Asset::getId, Function.identity()));

        return recommendations.stream()
                .map(recommendation -> RecommendationResponseDto.fromEntity(
                        recommendation,
                        assetsById.get(recommendation.getAssetId())
                ))
                .toList();
    }

    @Transactional
    public void deleteOldRecommendations(String userId) {
        validateUserId(userId);
        recommendationRepository.deleteByUserId(userId);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new RecomendacaoException("UserId e obrigatorio");
        }
    }
}
