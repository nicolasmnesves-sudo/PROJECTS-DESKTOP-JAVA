package com.controleestoque.data;

import com.controleestoque.model.Produto;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final Conexao conexao = new Conexao();

    private static final String SELECT_BASE =
            "SELECT p.id, p.codigo, p.nome, p.id_categoria, c.nome AS nome_categoria, " +
            "       p.preco_compra, p.preco_venda, p.estoque, p.estoque_minimo, " +
            "       p.ativo, p.data_cadastro " +
            "FROM produtos p " +
            "INNER JOIN categorias c ON c.id = p.id_categoria ";

    /** Insere o produto. Regra de negócio: todo produto nasce com saldo (estoque) zero. */
    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos " +
                "(codigo, nome, id_categoria, preco_compra, preco_venda, estoque, estoque_minimo, ativo) " +
                "VALUES (?, ?, ?, ?, ?, 0, ?, ?)";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setInt(3, produto.getIdCategoria());
            stmt.setBigDecimal(4, produto.getPrecoCompra());
            stmt.setBigDecimal(5, produto.getPrecoVenda());
            stmt.setInt(6, produto.getEstoqueMinimo());
            stmt.setBoolean(7, produto.isAtivo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
            produto.setEstoque(0);
        }
    }

    /** Atualiza os dados cadastrais do produto. O campo "estoque" nunca é alterado aqui. */
    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET codigo = ?, nome = ?, id_categoria = ?, " +
                "preco_compra = ?, preco_venda = ?, estoque_minimo = ? WHERE id = ?";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setInt(3, produto.getIdCategoria());
            stmt.setBigDecimal(4, produto.getPrecoCompra());
            stmt.setBigDecimal(5, produto.getPrecoVenda());
            stmt.setInt(6, produto.getEstoqueMinimo());
            stmt.setInt(7, produto.getId());
            stmt.executeUpdate();
        }
    }

    /** Ativa ou desativa um produto (produtos desativados não recebem novas movimentações). */
    public void alterarSituacao(int id, boolean ativo) throws SQLException {
        String sql = "UPDATE produtos SET ativo = ? WHERE id = ?";
        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setBoolean(1, ativo);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Produto buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<Produto> listar() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY p.nome";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Pesquisa por código ou nome (like). */
    public List<Produto> pesquisar(String termo) throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE p.codigo LIKE ? OR p.nome LIKE ? ORDER BY p.nome";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            String like = "%" + termo + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    /** Lista produtos ativos cujo estoque atual é menor ou igual ao estoque mínimo. */
    public List<Produto> listarEstoqueBaixo() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE p.estoque <= p.estoque_minimo AND p.ativo = 1 ORDER BY p.nome";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNome(rs.getString("nome"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setNomeCategoria(rs.getString("nome_categoria"));
        p.setPrecoCompra(rs.getBigDecimal("preco_compra"));
        p.setPrecoVenda(rs.getBigDecimal("preco_venda"));
        p.setEstoque(rs.getInt("estoque"));
        p.setEstoqueMinimo(rs.getInt("estoque_minimo"));
        p.setAtivo(rs.getBoolean("ativo"));
        Timestamp ts = rs.getTimestamp("data_cadastro");
        if (ts != null) {
            p.setDataCadastro(ts.toLocalDateTime());
        }
        return p;
    }
}
