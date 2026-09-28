package com.acessorinvestimentos.recomendacoes.service;

import com.acessorinvestimentos.recomendacoes.dto.MarketDataDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MarketDataServiceTests {

    @InjectMocks
    private MarketDataService marketDataService;

    @Mock
    private AssetService assetService;

    @Test
    void shouldReturnMarketData() {
        LocalDateTime updateTime = LocalDateTime.now();
        Asset asset = new Asset();
        asset.setTicker("PETR4");
        asset.setCurrentPrice(new BigDecimal("38.75"));
        asset.setLastPriceUpdate(updateTime);
        Mockito.when(assetService.getAssetByTicker("petr4")).thenReturn(asset);

        MarketDataDto result = marketDataService.getMarketData("petr4");

        assertEquals("PETR4", result.getTicker());
        assertEquals(new BigDecimal("38.75"), result.getCurrentPrice());
        assertEquals(updateTime, result.getUpdatedAt());
    }

    @Test
    void shouldUpdateAssetPrice() {
        Asset updated = new Asset();
        BigDecimal price = new BigDecimal("40.10");
        Mockito.when(assetService.updateAssetPrice("PETR4", price)).thenReturn(updated);

        Asset result = marketDataService.updateAssetPrice("PETR4", price);

        assertSame(updated, result);
    }

    @Test
    void shouldRejectNullPrice() {
        assertThrows(RecomendacaoException.class, () -> marketDataService.updateAssetPrice("PETR4", null));
        Mockito.verifyNoInteractions(assetService);
    }

    @Test
    void shouldUpdateAllAssetPrices() {
        Asset first = asset("PETR4", "38.75");
        Asset second = asset("VALE3", "65.30");
        Mockito.when(assetService.getAllAssets()).thenReturn(List.of(first, second));
        Mockito.when(assetService.updateAssetPrice("PETR4", first.getCurrentPrice())).thenReturn(first);
        Mockito.when(assetService.updateAssetPrice("VALE3", second.getCurrentPrice())).thenReturn(second);

        List<Asset> result = marketDataService.updateAllAssetPrices();

        assertEquals(List.of(first, second), result);
        Mockito.verify(assetService).updateAssetPrice("PETR4", first.getCurrentPrice());
        Mockito.verify(assetService).updateAssetPrice("VALE3", second.getCurrentPrice());
    }

    private Asset asset(String ticker, String price) {
        Asset asset = new Asset();
        asset.setTicker(ticker);
        asset.setCurrentPrice(new BigDecimal(price));
        return asset;
    }
}
