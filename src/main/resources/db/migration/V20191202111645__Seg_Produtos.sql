CREATE TABLE seg_produtos
(    
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome_produto         VARCHAR(100) NOT NULL,
    cod_produto          VARCHAR(100)     NULL,
    preco_produto        FLOAT            NULL,
    id_produtos_linhas  BIGINT        NOT NULL
    FOREIGN KEY REFERENCES seg_Linhas (id),
    unipercax_produto    FLOAT            NULL,
    pesoperuni_produto   FLOAT            NULL,
    validade_produto     DATE             NULL,
    UNIQUE ("id_Produtos_linhas", "nome_Produto")
);
