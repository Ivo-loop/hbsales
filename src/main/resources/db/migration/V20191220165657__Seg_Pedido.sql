CREATE TABLE seg_pedido
(
    id BIGINT           IDENTITY(1,1) PRIMARY KEY,
    cod_pedido          VARCHAR(10)   NOT     NULL,
    status_pedido       VARCHAR(10)   DEFAULT ('Ativo')
    CHECK(status_pedido IN ('Ativo','Cancelado','Retirado')),
    id_pedido_fornecedo BIGINT        NOT     NULL
        FOREIGN KEY REFERENCES seg_fornecedores (id),
    id_pedido_produtos  BIGINT        NOT     NULL
        FOREIGN KEY REFERENCES seg_produtos (id),
    dia_create_pedido   DATE NULL DEFAULT CURRENT_TIMESTAMP,
    amount_pedido       INT           NOT     NULL
);