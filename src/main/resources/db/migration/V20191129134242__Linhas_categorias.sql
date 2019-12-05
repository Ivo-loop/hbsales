CREATE TABLE seg_Linhas
(    
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome_Linhas         VARCHAR(10) NOT NULL,
    cod_Linhas          VARCHAR(100)    NULL,
    id_Linhas_Categoria  BIGINT     NOT NULL
    FOREIGN KEY REFERENCES seg_categoria (id),


);

ALTER TABLE "dbo"."seg_Linhas"
ADD UNIQUE ("id_Linhas_Categoria", "nome_Linhas");