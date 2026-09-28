# Acessor de Investimentos - Recomendacoes

Servico Spring Boot responsavel por manter ativos disponiveis e gerar recomendacoes
compativeis com o perfil do investidor.

## Principais endpoints

```text
GET  /assets
GET  /assets/{ticker}
POST /assets
PUT  /assets/{ticker}

POST /recommendations/{userId}/generate
GET  /recommendations/{userId}
```

## Configuracao

```properties
DB_HOST=localhost
DB_NAME=recomendacoesdb
DB_USER=postgres
DB_PASSWORD=postgres
PERFIL_SERVICE_URL=http://localhost:8081
```

## Testes

```bash
mvn verify
```

O relatorio JaCoCo e gerado em `tests/`, seguindo o mesmo padrao usado no
repositorio de pagamentos. O build exige pelo menos 80% de cobertura de
instrucoes no projeto e em cada classe do pacote `service`.
