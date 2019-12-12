CREATE TABLE seg_categoria
(    
    id BIGINT IDENTITY(1,1)          PRIMARY KEY,
    nome_categoria   VARCHAR(50)        NOT NULL,
    cod_categoria    VARCHAR(10) UNIQUE NOT NULL,
    id_categoria_fornecedor  BIGINT FOREIGN KEY REFERENCES seg_fornecedores (id) NOT NULL
);

ALTER TABLE "dbo"."seg_categoria"
ADD UNIQUE ("id_categoria_fornecedor", "nome_categoria");