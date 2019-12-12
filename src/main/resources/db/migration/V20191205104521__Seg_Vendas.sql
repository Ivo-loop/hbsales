CREATE TABLE seg_vendas
(    
    id BIGINT IDENTITY(1,1)              PRIMARY KEY,
    descricao_vendas           VARCHAR(50)      NULL,
    dia_inicial_vendas         DATE             NULL,
    dia_final_vendas           DATE             NULL,
    dia_retirada_vendas        DATE             NULL,
    id_vendas_fornecedor       BIGINT       NOT NULL
    FOREIGN KEY REFERENCES seg_fornecedores (id)
);