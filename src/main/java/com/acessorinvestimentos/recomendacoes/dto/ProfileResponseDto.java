package com.acessorinvestimentos.recomendacoes.dto;

import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private String userId;
    private InvestorType investorType;
}
