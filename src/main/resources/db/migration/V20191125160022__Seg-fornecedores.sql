create table seg_fornecedores
(
      id_fornecedores     BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
      razao_social VARCHAR(100)           NOT NULL,
      cnpj         VARCHAR(14)    UNIQUE  NOT NULL,
      nome_fan     VARCHAR(100)           NOT NULL,
      endereco     VARCHAR(500)           UNIQUE,
      telefone     VARCHAR(11)            UNIQUE,
      email        VARCHAR(500)           UNIQUE,
);