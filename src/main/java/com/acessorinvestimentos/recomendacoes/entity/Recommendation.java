package com.acessorinvestimentos.recomendacoes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private UUID assetId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvestorType investorType;

    @Column(nullable = false)
    private Integer compatibilityScore;

    @Column(nullable = false, length = 500)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static Recommendation fromAsset(
            String userId,
            InvestorType investorType,
            Asset asset,
            Integer compatibilityScore,
            String reason
    ) {
        Recommendation recommendation = new Recommendation();
        recommendation.setUserId(userId);
        recommendation.setInvestorType(investorType);
        recommendation.setAssetId(asset.getId());
        recommendation.setCompatibilityScore(compatibilityScore);
        recommendation.setReason(reason);
        return recommendation;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
