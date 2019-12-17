CREATE TABLE seg_funcionario
(
    id BIGINT IDENTITY(1,1)         PRIMARY KEY,
    nome_funcionario  VARCHAR(50)   NOT NULL,
    email_funcionario VARCHAR(50)   NOT NULL,
    uuid_funcionario  VARCHAR(36)   NOT NULL
);