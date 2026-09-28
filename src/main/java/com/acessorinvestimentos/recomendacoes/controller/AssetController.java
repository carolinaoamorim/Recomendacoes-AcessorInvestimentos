package com.acessorinvestimentos.recomendacoes.controller;

import com.acessorinvestimentos.recomendacoes.dto.AssetDto;
import com.acessorinvestimentos.recomendacoes.entity.Asset;
import com.acessorinvestimentos.recomendacoes.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Asset createAsset(@RequestBody AssetDto dto) {
        return assetService.createAsset(dto);
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{ticker}")
    public Asset getAssetByTicker(@PathVariable String ticker) {
        return assetService.getAssetByTicker(ticker);
    }

    @PutMapping("/{ticker}")
    public Asset updateAsset(@PathVariable String ticker, @RequestBody AssetDto dto) {
        return assetService.updateAsset(ticker, dto);
    }
}
