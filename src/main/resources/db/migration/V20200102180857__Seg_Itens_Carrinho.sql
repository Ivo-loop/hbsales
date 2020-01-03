CREATE table seg_itens_Carrinho(
    id BIGINT IDENTITY(1,1)         PRIMARY KEY,
    id_itens_carrinho    BIGINT        NOT     NULL
        FOREIGN KEY REFERENCES seg_carrinho (id),
    id_itens_produto  BIGINT        NOT     NULL
        FOREIGN KEY REFERENCES seg_produtos (id),
    amount_itens       INT           NOT     NULL
)