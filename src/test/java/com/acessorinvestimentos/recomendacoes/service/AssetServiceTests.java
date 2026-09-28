package com.acessorinvestimentos.recomendacoes.service;

import com.acessorinvestimentos.recomendacoes.dto.AssetDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.entity.AssetType;
import com.acessorinvestimentos.recomendacoes.entity.RiskLevel;
import com.acessorinvestimentos.recomendacoes.exception.RecomendacaoException;
import com.acessorinvestimentos.recomendacoes.repository.AssetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTests {

    @InjectMocks
    private AssetService assetService;

    @Mock
    private AssetRepository assetRepository;

    @Test
    public void test_shouldCreateAsset() {
        AssetDto dto = createAssetDto("PETR4", RiskLevel.MEDIUM);
        Asset asset = Asset.fromDto(dto);
        asset.setId(UUID.randomUUID());

        Mockito.when(assetRepository.existsByTickerIgnoreCase("PETR4")).thenReturn(false);
        Mockito.when(assetRepository.save(Mockito.any())).thenReturn(asset);

        Asset response = assetService.createAsset(dto);

        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals("PETR4", response.getTicker());
        Assertions.assertEquals(RiskLevel.MEDIUM, response.getRiskLevel());
        Assertions.assertTrue(response.getActive());
    }

    @Test
    public void test_shouldReturnAssetWhenCallGetAssetByTicker() {
        Asset asset = Asset.fromDto(createAssetDto("CDB001", RiskLevel.LOW));

        Mockito.when(assetRepository.findByTickerIgnoreCase("CDB001"))
                .thenReturn(Optional.of(asset));

        Asset response = assetService.getAssetByTicker("cdb001");

        Assertions.assertEquals("CDB001", response.getTicker());
        Assertions.assertEquals(RiskLevel.LOW, response.getRiskLevel());
    }

    @Test
    public void test_shouldReturnAllAssets() {
        List<Asset> assets = List.of(
                Asset.fromDto(createAssetDto("PETR4", RiskLevel.MEDIUM)),
                Asset.fromDto(createAssetDto("TESOURO", RiskLevel.LOW))
        );

        Mockito.when(assetRepository.findAll()).thenReturn(assets);

        List<Asset> response = assetService.getAllAssets();

        Assertions.assertEquals(2, response.size());
    }

    @Test
    public void test_shouldUpdateAssetPrice() {
        Asset asset = Asset.fromDto(createAssetDto("BOVA11", RiskLevel.MEDIUM));

        Mockito.when(assetRepository.findByTickerIgnoreCase("BOVA11"))
                .thenReturn(Optional.of(asset));
        Mockito.when(assetRepository.save(Mockito.any())).thenReturn(asset);

        Asset response = assetService.updateAssetPrice("bova11", new BigDecimal("132.50"));

        Assertions.assertEquals(new BigDecimal("132.50"), response.getCurrentPrice());
        Assertions.assertNotNull(response.getLastPriceUpdate());
    }

    @Test
    public void test_shouldRejectInvalidAssetData() {
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(null));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto(null, "Nome", AssetType.STOCK, RiskLevel.LOW, BigDecimal.ONE, null, true)
        ));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto("  ", "Nome", AssetType.STOCK, RiskLevel.LOW, BigDecimal.ONE, null, true)
        ));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto("ABCD3", null, AssetType.STOCK, RiskLevel.LOW, BigDecimal.ONE, null, true)
        ));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto("ABCD3", "  ", AssetType.STOCK, RiskLevel.LOW, BigDecimal.ONE, null, true)
        ));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto("ABCD3", "Nome", null, RiskLevel.LOW, BigDecimal.ONE, null, true)
        ));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto("ABCD3", "Nome", AssetType.STOCK, null, BigDecimal.ONE, null, true)
        ));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto("ABCD3", "Nome", AssetType.STOCK, RiskLevel.LOW, null, null, true)
        ));
        assertThrows(RecomendacaoException.class, () -> assetService.createAsset(
                new AssetDto("ABCD3", "Nome", AssetType.STOCK, RiskLevel.LOW, new BigDecimal("-0.01"), null, true)
        ));
    }

    @Test
    public void test_shouldRejectDuplicatedTicker() {
        Mockito.when(assetRepository.existsByTickerIgnoreCase("PETR4")).thenReturn(true);

        RecomendacaoException exception = assertThrows(
                RecomendacaoException.class,
                () -> assetService.createAsset(createAssetDto(" petr4 ", RiskLevel.MEDIUM))
        );

        assertTrue(exception.getMessage().contains("PETR4"));
        Mockito.verify(assetRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void test_shouldRejectMissingAssetAndInvalidTicker() {
        Mockito.when(assetRepository.findByTickerIgnoreCase("INEXISTENTE")).thenReturn(Optional.empty());

        assertThrows(RecomendacaoException.class, () -> assetService.getAssetByTicker("inexistente"));
        assertThrows(RecomendacaoException.class, () -> assetService.getAssetByTicker(null));
        assertThrows(RecomendacaoException.class, () -> assetService.getAssetByTicker(" "));
    }

    @Test
    public void test_shouldUpdateAllAssetFields() {
        Asset asset = Asset.fromDto(createAssetDto("PETR4", RiskLevel.MEDIUM));
        AssetDto update = new AssetDto(
                " vale3 ",
                "Vale",
                AssetType.FII,
                RiskLevel.HIGH,
                new BigDecimal("65.30"),
                "Mineracao",
                false
        );
        Mockito.when(assetRepository.findByTickerIgnoreCase("PETR4")).thenReturn(Optional.of(asset));
        Mockito.when(assetRepository.existsByTickerIgnoreCase("VALE3")).thenReturn(false);
        Mockito.when(assetRepository.save(asset)).thenReturn(asset);

        Asset response = assetService.updateAsset("petr4", update);

        assertEquals("VALE3", response.getTicker());
        assertEquals("Vale", response.getName());
        assertEquals(AssetType.FII, response.getAssetType());
        assertEquals(RiskLevel.HIGH, response.getRiskLevel());
        assertEquals(new BigDecimal("65.30"), response.getCurrentPrice());
        assertEquals("Mineracao", response.getSector());
        assertFalse(response.getActive());
        assertNotNull(response.getLastPriceUpdate());
    }

    @Test
    public void test_shouldKeepFieldsWhenUpdateValuesAreAbsent() {
        Asset asset = Asset.fromDto(createAssetDto("PETR4", RiskLevel.MEDIUM));
        AssetDto update = new AssetDto(" ", " ", null, null, null, null, null);
        Mockito.when(assetRepository.findByTickerIgnoreCase("PETR4")).thenReturn(Optional.of(asset));
        Mockito.when(assetRepository.save(asset)).thenReturn(asset);

        Asset response = assetService.updateAsset("PETR4", update);

        assertEquals("PETR4", response.getTicker());
        assertEquals("Ativo PETR4", response.getName());
        assertEquals(RiskLevel.MEDIUM, response.getRiskLevel());
        assertTrue(response.getActive());
    }

    @Test
    public void test_shouldRejectDuplicatedTickerOnUpdate() {
        Asset asset = Asset.fromDto(createAssetDto("PETR4", RiskLevel.MEDIUM));
        Mockito.when(assetRepository.findByTickerIgnoreCase("PETR4")).thenReturn(Optional.of(asset));
        Mockito.when(assetRepository.existsByTickerIgnoreCase("VALE3")).thenReturn(true);

        assertThrows(
                RecomendacaoException.class,
                () -> assetService.updateAsset("PETR4", new AssetDto("VALE3", null, null, null, null, null, null))
        );
    }

    @Test
    public void test_shouldRejectNegativePriceOnUpdate() {
        Asset asset = Asset.fromDto(createAssetDto("PETR4", RiskLevel.MEDIUM));
        Mockito.when(assetRepository.findByTickerIgnoreCase("PETR4")).thenReturn(Optional.of(asset));

        assertThrows(
                RecomendacaoException.class,
                () -> assetService.updateAsset(
                        "PETR4",
                        new AssetDto(null, null, null, null, new BigDecimal("-1"), null, null)
                )
        );
        assertThrows(RecomendacaoException.class, () -> assetService.updateAssetPrice("PETR4", null));
    }

    @Test
    public void test_shouldDeactivateAsset() {
        Asset asset = Asset.fromDto(createAssetDto("PETR4", RiskLevel.MEDIUM));
        Mockito.when(assetRepository.findByTickerIgnoreCase("PETR4")).thenReturn(Optional.of(asset));
        Mockito.when(assetRepository.save(asset)).thenReturn(asset);

        Asset response = assetService.deactivateAsset("PETR4");

        assertFalse(response.getActive());
        Mockito.verify(assetRepository).save(asset);
    }

    private AssetDto createAssetDto(String ticker, RiskLevel riskLevel) {
        return new AssetDto(
                ticker,
                "Ativo " + ticker,
                AssetType.STOCK,
                riskLevel,
                new BigDecimal("100.00"),
                "Financeiro",
                true
        );
    }
}
