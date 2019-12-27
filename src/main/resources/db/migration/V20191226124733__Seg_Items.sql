CREATE table seg_items(
    id BIGINT IDENTITY(1,1)         PRIMARY KEY,
    id_itens_pedido    BIGINT        NOT     NULL
        FOREIGN KEY REFERENCES seg_pedido (id),
    id_itens_produtos  BIGINT        NOT     NULL
        FOREIGN KEY REFERENCES seg_produtos (id),
    amount_itens       INT           NOT     NULL
)