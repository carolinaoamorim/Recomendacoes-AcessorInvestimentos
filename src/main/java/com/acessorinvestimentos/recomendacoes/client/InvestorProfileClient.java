package com.acessorinvestimentos.recomendacoes.client;

import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class InvestorProfileClient {

    private final RestTemplate restTemplate;

    @Value("${perfil.service.url:http://localhost:8081}")
    private String perfilServiceUrl;

    public ProfileResponseDto getInvestorProfile(String userId) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(perfilServiceUrl)
                    .path("/profiles/{userId}")
                    .buildAndExpand(userId)
                    .toUriString();

            ProfileResponseDto profile = restTemplate.getForObject(url, ProfileResponseDto.class);
            if (profile == null || profile.getInvestorType() == null) {
                throw new RecomendacaoException("Perfil do investidor nao encontrado");
            }
            return profile;
        } catch (RestClientException exception) {
            throw new RecomendacaoException("Nao foi possivel buscar o perfil do investidor");
        }
    }
}
