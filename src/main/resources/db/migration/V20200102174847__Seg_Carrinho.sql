CREATE TABLE seg_carrinho
(
    id BIGINT           IDENTITY(1,1) PRIMARY KEY,
    cod_pedido          VARCHAR(10)   NOT     NULL,
    id_pedido_fornecedo BIGINT        NOT     NULL
        FOREIGN KEY REFERENCES seg_fornecedores (id),
    id_pedido_funcionario BIGINT      NOT     NULL
        FOREIGN KEY REFERENCES seg_funcionario (id)
);