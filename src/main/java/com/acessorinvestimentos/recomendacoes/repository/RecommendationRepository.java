package com.acessorinvestimentos.recomendacoes.repository;

import com.acessorinvestimentos.recomendacoes.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {
    List<Recommendation> findByUserId(String userId);

    void deleteByUserId(String userId);
}
