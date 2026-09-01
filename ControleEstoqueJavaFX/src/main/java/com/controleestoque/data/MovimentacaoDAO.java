package com.controleestoque.data;

import com.controleestoque.model.Movimentacao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {

    private final Conexao conexao = new Conexao();

    private static final String SELECT_BASE =
            "SELECT m.id, m.id_produto, p.nome AS nome_produto, m.tipo, m.quantidade, " +
            "       m.data_movimentacao, m.observacao " +
            "FROM movimentacoes m " +
            "INNER JOIN produtos p ON p.id = m.id_produto ";

    /**
     * Registra uma movimentação (ENTRADA ou SAÍDA), atualiza o saldo do produto e
     * insere o histórico, tudo dentro de uma única transação.
     * Se qualquer etapa falhar, um Rollback desfaz todas as alterações.
     */
    public void registrar(Movimentacao mov) throws SQLException, EstoqueException {
        if (mov.getQuantidade() <= 0) {
            throw new EstoqueException("A quantidade deve ser maior que zero.");
        }
        if (!Movimentacao.ENTRADA.equals(mov.getTipo()) && !Movimentacao.SAIDA.equals(mov.getTipo())) {
            throw new EstoqueException("Tipo de movimentação inválido. Use ENTRADA ou SAIDA.");
        }

        try (Connection conn = conexao.criarConexao()) {
            conn.setAutoCommit(false);

            try {
                // 1. Consultar e validar o produto/estoque
                int estoqueAtual;
                boolean ativo;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT estoque, ativo FROM produtos WHERE id = ? FOR UPDATE")) {
                    stmt.setInt(1, mov.getIdProduto());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new EstoqueException("Produto não encontrado.");
                        }
                        estoqueAtual = rs.getInt("estoque");
                        ativo = rs.getBoolean("ativo");
                    }
                }

                if (!ativo) {
                    throw new EstoqueException("Não é possível movimentar um produto desativado.");
                }

                int novoEstoque;
                if (Movimentacao.ENTRADA.equals(mov.getTipo())) {
                    novoEstoque = estoqueAtual + mov.getQuantidade();
                } else {
                    if (mov.getQuantidade() > estoqueAtual) {
                        throw new EstoqueException(
                                "Saída não permitida: quantidade (" + mov.getQuantidade() +
                                        ") maior que o estoque disponível (" + estoqueAtual + ").");
                    }
                    novoEstoque = estoqueAtual - mov.getQuantidade();
                }

                // 2. Inserir a movimentação (histórico)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO movimentacoes (id_produto, tipo, quantidade, observacao) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, mov.getIdProduto());
                    stmt.setString(2, mov.getTipo());
                    stmt.setInt(3, mov.getQuantidade());
                    stmt.setString(4, mov.getObservacao());
                    stmt.executeUpdate();

                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            mov.setId(rs.getInt(1));
                        }
                    }
                }

                // 3. Atualizar o saldo do produto
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE produtos SET estoque = ? WHERE id = ?")) {
                    stmt.setInt(1, novoEstoque);
                    stmt.setInt(2, mov.getIdProduto());
                    stmt.executeUpdate();
                }

                conn.commit();

            } catch (SQLException | EstoqueException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Movimentacao> listar() throws SQLException {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY m.data_movimentacao DESC";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Movimentacao> listarPorProduto(int idProduto) throws SQLException {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE m.id_produto = ? ORDER BY m.data_movimentacao DESC";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Movimentacao> listarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE DATE(m.data_movimentacao) BETWEEN ? AND ? " +
                "ORDER BY m.data_movimentacao DESC";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    /** Últimas movimentações realizadas, usado no painel principal. */
    public List<Movimentacao> listarUltimas(int limite) throws SQLException {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY m.data_movimentacao DESC LIMIT ?";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, limite);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    private Movimentacao mapear(ResultSet rs) throws SQLException {
        Movimentacao m = new Movimentacao();
        m.setId(rs.getInt("id"));
        m.setIdProduto(rs.getInt("id_produto"));
        m.setNomeProduto(rs.getString("nome_produto"));
        m.setTipo(rs.getString("tipo"));
        m.setQuantidade(rs.getInt("quantidade"));
        Timestamp ts = rs.getTimestamp("data_movimentacao");
        if (ts != null) {
            m.setDataMovimentacao(ts.toLocalDateTime());
        }
        m.setObservacao(rs.getString("observacao"));
        return m;
    }
}
