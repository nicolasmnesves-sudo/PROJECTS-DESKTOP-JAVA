package nicolasmneves.sistemacadastroclientes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Executa as operações de INSERT, SELECT, UPDATE e DELETE
 * na tabela "clientes".
 */
public class ClienteDAO {

    private final Conexao conexao = new Conexao();

    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, cpf, email, telefone, cidade, ativo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencherParametros(stmt, cliente);
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    cliente.setId(chaves.getInt(1));
                }
            }
        }
    }

    public void atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, cpf = ?, email = ?, telefone = ?, "
                + "cidade = ?, ativo = ? WHERE id = ?";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            preencherParametros(stmt, cliente);
            stmt.setInt(7, cliente.getId());
            stmt.executeUpdate();
        }
    }

    public void alterarSituacao(int id, boolean ativo) throws SQLException {
        String sql = "UPDATE clientes SET ativo = ? WHERE id = ?";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setBoolean(1, ativo);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }
        return clientes;
    }

    public List<Cliente> pesquisarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE nome LIKE ? ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        }
        return clientes;
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    /**
     * Verifica se já existe um cliente com o CPF informado.
     * Ao editar, informe idAtual para não considerar o próprio registro.
     */
    public boolean cpfExistente(String cpf, Integer idAtual) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE cpf = ? AND id <> ?";

        try (Connection con = conexao.criarConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setInt(2, idAtual == null ? 0 : idAtual);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void preencherParametros(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getCpf());
        stmt.setString(3, cliente.getEmail());
        stmt.setString(4, cliente.getTelefone());
        stmt.setString(5, cliente.getCidade());
        stmt.setBoolean(6, cliente.isAtivo());
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setCidade(rs.getString("cidade"));
        cliente.setAtivo(rs.getBoolean("ativo"));

        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            cliente.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        return cliente;
    }
}
