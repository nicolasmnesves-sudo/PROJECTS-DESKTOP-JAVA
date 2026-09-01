package com.controleestoque.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsável por abrir conexões JDBC com o banco MySQL "db_controle_estoque".
 * Ajuste HOST, PORTA, USUARIO e SENHA conforme o seu ambiente.
 */
public class Conexao {

    private static final String HOST = "localhost";
    private static final String PORTA = "3306";
    private static final String BANCO = "db_controle_estoque";
    private static final String USUARIO = "root";
    private static final String SENHA = "root";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORTA + "/" + BANCO
                    + "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";

    public Connection criarConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do MySQL não encontrado no classpath.", e);
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
