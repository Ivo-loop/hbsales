CREATE TABLE seg_Linhas
(    
    id BIGINT IDENTITY(1,1)      PRIMARY KEY,
    nome_Linhas         VARCHAR(50)        NOT NULL,
    cod_Linhas          VARCHAR(10) UNIQUE NOT NULL,
    id_Linhas_Categoria  BIGINT            NOT NULL
    FOREIGN KEY REFERENCES seg_categoria (id),


);

ALTER TABLE "dbo"."seg_Linhas"
ADD UNIQUE ("id_Linhas_Categoria", "nome_Linhas");