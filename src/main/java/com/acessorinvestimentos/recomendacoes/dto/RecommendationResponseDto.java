package com.acessorinvestimentos.recomendacoes.dto;

import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.AssetType;
import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.entity.Recommendation;
import com.acessorinvestimentos.recomendacoes.entity.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseDto {
    private UUID id;
    private String userId;
    private UUID assetId;
    private String ticker;
    private String assetName;
    private AssetType assetType;
    private RiskLevel riskLevel;
    private BigDecimal currentPrice;
    private InvestorType investorType;
    private Integer compatibilityScore;
    private String reason;
    private LocalDateTime createdAt;

    public static RecommendationResponseDto fromEntity(Recommendation recommendation, Asset asset) {
        RecommendationResponseDto dto = new RecommendationResponseDto();
        dto.setId(recommendation.getId());
        dto.setUserId(recommendation.getUserId());
        dto.setAssetId(recommendation.getAssetId());
        dto.setInvestorType(recommendation.getInvestorType());
        dto.setCompatibilityScore(recommendation.getCompatibilityScore());
        dto.setReason(recommendation.getReason());
        dto.setCreatedAt(recommendation.getCreatedAt());

        if (asset != null) {
            dto.setTicker(asset.getTicker());
            dto.setAssetName(asset.getName());
            dto.setAssetType(asset.getAssetType());
            dto.setRiskLevel(asset.getRiskLevel());
            dto.setCurrentPrice(asset.getCurrentPrice());
        }

        return dto;
    }
}
