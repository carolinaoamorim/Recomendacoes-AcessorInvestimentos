package com.acessorinvestimentos.recomendacoes.client;

import com.acessorinvestimentos.recomendacoes.dto.ProfileResponseDto;
import com.acessorinvestimentos.recomendacoes.entity.InvestorType;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class InvestorProfileClientTests {

    @Mock
    private RestTemplate restTemplate;

    private InvestorProfileClient client;

    @BeforeEach
    void setUp() {
        client = new InvestorProfileClient(restTemplate);
        ReflectionTestUtils.setField(client, "perfilServiceUrl", "http://perfil:8081");
    }

    @Test
    void shouldReturnInvestorProfile() {
        ProfileResponseDto profile = new ProfileResponseDto("user-1", InvestorType.MODERATE);
        Mockito.when(restTemplate.getForObject(anyString(), eq(ProfileResponseDto.class))).thenReturn(profile);

        ProfileResponseDto result = client.getInvestorProfile("user-1");

        assertSame(profile, result);
        Mockito.verify(restTemplate).getForObject("http://perfil:8081/profiles/user-1", ProfileResponseDto.class);
    }

    @Test
    void shouldRejectNullProfile() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(ProfileResponseDto.class))).thenReturn(null);

        RecomendacaoException exception = assertThrows(
                RecomendacaoException.class,
                () -> client.getInvestorProfile("user-1")
        );

        assertEquals("Perfil do investidor nao encontrado", exception.getMessage());
    }

    @Test
    void shouldRejectProfileWithoutInvestorType() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(ProfileResponseDto.class)))
                .thenReturn(new ProfileResponseDto("user-1", null));

        assertThrows(RecomendacaoException.class, () -> client.getInvestorProfile("user-1"));
    }

    @Test
    void shouldTranslateRestClientFailure() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(ProfileResponseDto.class)))
                .thenThrow(new RestClientException("offline"));

        RecomendacaoException exception = assertThrows(
                RecomendacaoException.class,
                () -> client.getInvestorProfile("user-1")
        );

        assertTrue(exception.getMessage().contains("Nao foi possivel"));
    }
}
