package com.acessorinvestimentos.recomendacoes.service;

import com.acessorinvestimentos.recomendacoes.dto.MarketDataDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final AssetService assetService;

    public MarketDataDto getMarketData(String ticker) {
        Asset asset = assetService.getAssetByTicker(ticker);
        return new MarketDataDto(asset.getTicker(), asset.getCurrentPrice(), asset.getLastPriceUpdate());
    }

    public Asset updateAssetPrice(String ticker, BigDecimal currentPrice) {
        if (currentPrice == null) {
            throw new RecomendacaoException("Preco atual e obrigatorio");
        }
        return assetService.updateAssetPrice(ticker, currentPrice);
    }

    public List<Asset> updateAllAssetPrices() {
        return assetService.getAllAssets().stream()
                .map(asset -> assetService.updateAssetPrice(asset.getTicker(), asset.getCurrentPrice()))
                .toList();
    }
}
