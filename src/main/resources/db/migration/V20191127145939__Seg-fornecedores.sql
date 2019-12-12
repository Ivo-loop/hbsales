create table seg_fornecedores
(
      id    BIGINT IDENTITY(1,1)       PRIMARY KEY,
      razao_social VARCHAR(100)           NOT NULL,
      cnpj         VARCHAR(14)    UNIQUE  NOT NULL,
      nome_fan     VARCHAR(100)           NOT NULL,
      endereco     VARCHAR(100)           NOT NULL,
      telefone     VARCHAR(14)            NOT NULL,
      email        VARCHAR(50)            NOT NULL,
);