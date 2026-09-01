CREATE DATABASE IF NOT EXISTS db_controle_estoque
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE db_controle_estoque;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS movimentacoes;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS categorias;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE categorias
(
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_categorias_nome (nome)
);

CREATE TABLE produtos
(
    id INT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(30) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    id_categoria INT NOT NULL,
    preco_compra DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    preco_venda DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estoque INT NOT NULL DEFAULT 0,
    estoque_minimo INT NOT NULL DEFAULT 0,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_produtos_codigo (codigo),
    CONSTRAINT fk_produtos_categorias
        FOREIGN KEY (id_categoria)
        REFERENCES categorias (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE movimentacoes
(
    id INT NOT NULL AUTO_INCREMENT,
    id_produto INT NOT NULL,
    tipo ENUM('ENTRADA', 'SAIDA') NOT NULL,
    quantidade INT NOT NULL,
    data_movimentacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacao VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_movimentacoes_produtos
        FOREIGN KEY (id_produto)
        REFERENCES produtos (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX idx_produtos_nome ON produtos (nome);
CREATE INDEX idx_movimentacoes_data ON movimentacoes (data_movimentacao);
CREATE INDEX idx_movimentacoes_produto ON movimentacoes (id_produto);

-- Dados iniciais
INSERT INTO categorias (nome) VALUES
('Informática'),
('Acessórios'),
('Escritório'),
('Redes');

-- Todo produto nasce com estoque = 0. O saldo inicial é gerado por movimentações de entrada.
INSERT INTO produtos
(codigo, nome, id_categoria, preco_compra, preco_venda, estoque, estoque_minimo, ativo)
VALUES
('INF-001', 'Teclado USB', 2, 45.00, 79.90, 0, 5, 1),
('INF-002', 'Mouse Óptico USB', 2, 25.00, 49.90, 0, 5, 1),
('RED-001', 'Cabo de Rede CAT6', 4, 12.00, 24.90, 0, 10, 1),
('ESC-001', 'Suporte para Notebook', 3, 55.00, 99.90, 0, 5, 1);

-- Movimentações que formam o estoque inicial
INSERT INTO movimentacoes (id_produto, tipo, quantidade, observacao)
VALUES
(1, 'ENTRADA', 10, 'Estoque inicial'),
(2, 'ENTRADA', 8, 'Estoque inicial'),
(3, 'ENTRADA', 20, 'Estoque inicial'),
(4, 'ENTRADA', 3, 'Estoque inicial');

-- Atualização coerente do saldo com base nas entradas iniciais
UPDATE produtos SET estoque = 10 WHERE id = 1;
UPDATE produtos SET estoque = 8  WHERE id = 2;
UPDATE produtos SET estoque = 20 WHERE id = 3;
UPDATE produtos SET estoque = 3  WHERE id = 4;

-- Views auxiliares (opcionais, úteis para consultas e relatórios)
CREATE OR REPLACE VIEW vw_produtos_completos AS
SELECT
    p.id,
    p.codigo,
    p.nome,
    p.id_categoria,
    c.nome AS categoria,
    p.preco_compra,
    p.preco_venda,
    p.estoque,
    p.estoque_minimo,
    p.ativo,
    p.data_cadastro,
    CASE
        WHEN p.estoque <= p.estoque_minimo THEN 'ESTOQUE BAIXO'
        ELSE 'ESTOQUE NORMAL'
    END AS situacao_estoque
FROM produtos p
INNER JOIN categorias c ON c.id = p.id_categoria;

CREATE OR REPLACE VIEW vw_movimentacoes_completas AS
SELECT
    m.id,
    m.id_produto,
    p.codigo,
    p.nome AS produto,
    m.tipo,
    m.quantidade,
    m.data_movimentacao,
    m.observacao
FROM movimentacoes m
INNER JOIN produtos p ON p.id = m.id_produto;
