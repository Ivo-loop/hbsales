CREATE TABLE seg_produtos
(    
    id BIGINT IDENTITY(1,1)              PRIMARY KEY,
    nome_produto        VARCHAR(200)        NOT NULL,
    cod_produto         VARCHAR(10) UNIQUE  NOT NULL,
    preco_produto       DECIMAL(20,2)       NOT NULL,
    id_produtos_linhas  BIGINT              NOT NULL
    FOREIGN KEY REFERENCES seg_Linhas (id),
    unipercax_produto   FLOAT               NOT NULL,
    pesoperuni_produto  DECIMAL(30,3)       NOT NULL,
    uni_med_produto     VARCHAR(2)          NOT NULL
    CHECK       (uni_med_produto IN ('mg','g','kg')),
    validade_produto    DATE                NOT NULL,
    UNIQUE    ("id_Produtos_linhas", "nome_Produto")
);
