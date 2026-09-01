package nicolasmneves.sistemacadastroclientes;

import javafx.beans.property.*;

import java.time.LocalDateTime;

/**
 * Representa um cliente cadastrado no banco de dados.
 * Usa propriedades JavaFX para poder ser exibido diretamente no TableView.
 */
public class Cliente {

    private final IntegerProperty id = new SimpleIntegerProperty(0);
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty cpf = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telefone = new SimpleStringProperty();
    private final StringProperty cidade = new SimpleStringProperty();
    private final BooleanProperty ativo = new SimpleBooleanProperty(true);
    private final ObjectProperty<LocalDateTime> dataCadastro = new SimpleObjectProperty<>();

    public Cliente() {
    }

    public Cliente(String nome, String cpf, String email, String telefone, String cidade, boolean ativo) {
        setNome(nome);
        setCpf(cpf);
        setEmail(email);
        setTelefone(telefone);
        setCidade(cidade);
        setAtivo(ativo);
    }

    // ----- id -----
    public int getId() { return id.get(); }
    public void setId(int valor) { id.set(valor); }
    public IntegerProperty idProperty() { return id; }

    // ----- nome -----
    public String getNome() { return nome.get(); }
    public void setNome(String valor) { nome.set(valor); }
    public StringProperty nomeProperty() { return nome; }

    // ----- cpf -----
    public String getCpf() { return cpf.get(); }
    public void setCpf(String valor) { cpf.set(valor); }
    public StringProperty cpfProperty() { return cpf; }

    // ----- email -----
    public String getEmail() { return email.get(); }
    public void setEmail(String valor) { email.set(valor); }
    public StringProperty emailProperty() { return email; }

    // ----- telefone -----
    public String getTelefone() { return telefone.get(); }
    public void setTelefone(String valor) { telefone.set(valor); }
    public StringProperty telefoneProperty() { return telefone; }

    // ----- cidade -----
    public String getCidade() { return cidade.get(); }
    public void setCidade(String valor) { cidade.set(valor); }
    public StringProperty cidadeProperty() { return cidade; }

    // ----- ativo -----
    public boolean isAtivo() { return ativo.get(); }
    public void setAtivo(boolean valor) { ativo.set(valor); }
    public BooleanProperty ativoProperty() { return ativo; }

    // ----- dataCadastro -----
    public LocalDateTime getDataCadastro() { return dataCadastro.get(); }
    public void setDataCadastro(LocalDateTime valor) { dataCadastro.set(valor); }
    public ObjectProperty<LocalDateTime> dataCadastroProperty() { return dataCadastro; }
}
