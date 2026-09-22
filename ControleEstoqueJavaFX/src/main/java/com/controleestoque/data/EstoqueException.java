package com.controleestoque.data;

/**
 * Exceção lançada quando uma regra de negócio do estoque é violada
 * (ex.: saída maior que o saldo disponível, produto desativado, quantidade inválida).
 */
public class EstoqueException extends Exception {
    public EstoqueException(String message) {
        super(message);
    }
}
