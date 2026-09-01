CREATE DATABASE IF NOT EXISTS ds_cadastro_clientes
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE ds_cadastro_clientes;

DROP TABLE IF EXISTS clientes;

CREATE TABLE clientes
(
    id             INT NOT NULL AUTO_INCREMENT,
    nome           VARCHAR(120) NOT NULL,
    cpf            VARCHAR(14) NOT NULL,
    email          VARCHAR(120),
    telefone       VARCHAR(20),
    cidade         VARCHAR(80),
    ativo          TINYINT(1) NOT NULL DEFAULT 1,
    data_cadastro  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_clientes_cpf (cpf)
);

INSERT INTO clientes
    (nome, cpf, email, telefone, cidade, ativo)
VALUES
    ('Ana Souza', '111.111.111-11', 'ana@email.com', '(31) 99999-1111', 'Belo Horizonte', 1),
    ('Carlos Oliveira', '222.222.222-22', 'carlos@email.com', '(31) 99999-2222', 'Contagem', 1),
    ('Mariana Lima', '333.333.333-33', 'mariana@email.com', '(31) 99999-3333', 'Betim', 0);
