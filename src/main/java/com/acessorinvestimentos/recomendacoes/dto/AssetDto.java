package com.acessorinvestimentos.recomendacoes.dto;

import com.acessorinvestimentos.recomendacoes.entity.AssetType;
import com.acessorinvestimentos.recomendacoes.entity.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {
    private String ticker;
    private String name;
    private AssetType assetType;
    private RiskLevel riskLevel;
    private BigDecimal currentPrice;
    private String sector;
    private Boolean active;
}
