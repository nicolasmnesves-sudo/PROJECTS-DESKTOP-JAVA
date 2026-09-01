package nicolasmneves.sistemacadastroclientes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsável por criar conexões com o banco ds_cadastro_clientes.
 *
 * Requer o driver JDBC do MySQL (mysql-connector-j) no classpath do projeto.
 * Ajuste host, porta, usuário e senha conforme o seu ambiente.
 */
public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/ds_cadastro_clientes?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public Connection criarConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
