package nicolasmneves.estacionamento;

import java.time.LocalDateTime;
import javafx.beans.property.*;

public class Permanencia {
    private final StringProperty placa = new SimpleStringProperty();
    private final StringProperty modelo = new SimpleStringProperty();
    private final StringProperty tipo = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> entrada = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> saida = new SimpleObjectProperty<>();
    private final IntegerProperty horas = new SimpleIntegerProperty();
    private final DoubleProperty valor = new SimpleDoubleProperty();
    private final StringProperty situacao = new SimpleStringProperty();

    public Permanencia(String placa, String modelo, String tipo, LocalDateTime entrada) {
        this.placa.set(placa);
        this.modelo.set(modelo);
        this.tipo.set(tipo);
        this.entrada.set(entrada);
        this.horas.set(0);
        this.valor.set(0.0);
        this.situacao.set("Estacionado");
    }

    // Getters, Setters e Properties (necessários para o TableView)
    public String getPlaca() { return placa.get(); }
    public StringProperty placaProperty() { return placa; }

    public String getModelo() { return modelo.get(); }
    public StringProperty modeloProperty() { return modelo; }

    public String getTipo() { return tipo.get(); }
    public StringProperty tipoProperty() { return tipo; }

    public LocalDateTime getEntrada() { return entrada.get(); }
    public ObjectProperty<LocalDateTime> entradaProperty() { return entrada; }

    public LocalDateTime getSaida() { return saida.get(); }
    public void setSaida(LocalDateTime saida) { this.saida.set(saida); }
    public ObjectProperty<LocalDateTime> saidaProperty() { return saida; }

    public int getHoras() { return horas.get(); }
    public void setHoras(int horas) { this.horas.set(horas); }
    public IntegerProperty horasProperty() { return horas; }

    public double getValor() { return valor.get(); }
    public void setValor(double valor) { this.valor.set(valor); }
    public DoubleProperty valorProperty() { return valor; }

    public String getSituacao() { return situacao.get(); }
    public void setSituacao(String situacao) { this.situacao.set(situacao); }
    public StringProperty situacaoProperty() { return situacao; }
}