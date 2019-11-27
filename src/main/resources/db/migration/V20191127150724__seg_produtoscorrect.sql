CREATE TABLE seg_produtos(
    
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome_produto          VARCHAR(100)          NOT NULL,
    cod_produto           VARCHAR(100) UNIQUE   NOT NULL,
    id_produto_fornecedor  BIGINT
    FOREIGN KEY REFERENCES seg_fornecedores (id)
);