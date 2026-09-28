package com.acessorinvestimentos.recomendacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketDataDto {
    private String ticker;
    private BigDecimal currentPrice;
    private LocalDateTime updatedAt;
}
