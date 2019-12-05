CREATE TABLE seg_categoria
(    
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome_categoria          VARCHAR(10) NOT NULL,
    cod_categoria           VARCHAR(100)    NULL,
    id_categoria_fornecedor  BIGINT NOT NULL
    FOREIGN KEY REFERENCES seg_fornecedores (id),
);

ALTER TABLE "dbo"."seg_categoria"
ADD UNIQUE ("id_categoria_fornecedor", "nome_categoria");