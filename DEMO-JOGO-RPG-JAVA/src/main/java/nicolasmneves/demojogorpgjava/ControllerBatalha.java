package nicolasmneves.demojogorpgjava;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.Random;

public class ControllerBatalha {

    private Personagem personagem;
    private Personagem inimigo;

    private final Random random = new Random();

    @FXML
    private ImageView minhaImagem;

    @FXML
    private Button bntATA;

    @FXML
    private Button bntDEF;

    @FXML
    private Button bntHAB;

    @FXML
    private ProgressBar vidaPersonagem;

    @FXML
    private ProgressBar manaPersonagem;

    @FXML
    private ProgressBar vidaInimigo;

    @FXML
    private ProgressBar manaInimigo;

    @FXML
    private Label lblMensagem;

    @FXML
    public void initialize() {
        this.personagem = MenuController.personagemSelecionado;
        this.inimigo = new Inimigos();

        if (MenuController.caminhoImagem != null) {
            try {
                InputStream stream = getClass().getResourceAsStream(MenuController.caminhoImagem);
                if (stream != null) {
                    Image img = new Image(stream);
                    minhaImagem.setImage(img);
                } else {
                    lblMensagem.setText("Erro ao carregar imagem do personagem.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (validarEstado()) {
            atualizarBarras();
            lblMensagem.setText("A batalha começou! É o seu turno.");
        }
    }

    @FXML
    private void bntClickATA(ActionEvent actionEvent) {
        if (!validarEstado()) return;
        personagem.Atacar(inimigo);
        lblMensagem.setText("Você atacou o inimigo!");
        processarPosTurnoJogador();
    }

    @FXML
    private void bntClickDEF(ActionEvent actionEvent) {
        if (!validarEstado()) return;
        personagem.Defender();
        lblMensagem.setText("Você se preparou para defender!");
        processarPosTurnoJogador();
    }

    @FXML
    private void bntClickHAB(ActionEvent actionEvent) {
        if (!validarEstado()) return;
        personagem.UsarHabilidade(inimigo);
        lblMensagem.setText("Você usou uma habilidade!");
        processarPosTurnoJogador();
    }

    private void processarPosTurnoJogador() {
        atualizarBarras();

        // Se o inimigo morreu com a ação do jogador, encerra o combate
        if (batalhaAcabou()) return;

        // Desabilita os botões durante a espera
        alternarBotoes(true);

        // Aguarda 2 segundos antes da jogada do inimigo
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> executarTurnoInimigo());
        pause.play();
    }



    private void executarTurnoInimigo() {
        int escolha = random.nextInt(3); // 0 = ataque, 1 = defesa, 2 = habilidade

        switch (escolha) {
            case 0 -> {
                inimigo.Atacar(personagem);
                lblMensagem.setText("O inimigo atacou você!");
            }
            case 1 -> {
                inimigo.Defender();
                lblMensagem.setText("O inimigo entrou em posição defensiva!");
            }
            case 2 -> {
                inimigo.UsarHabilidade(personagem);
                lblMensagem.setText("O inimigo usou uma habilidade especial!");
            }
        }

        atualizarBarras();


        if (!batalhaAcabou()) {
            alternarBotoes(false);
        }
    }

    private void atualizarBarras() {
        vidaPersonagem.setProgress(clamp(personagem.getVida() / (double) personagem.getVidaMax()));
        manaPersonagem.setProgress(clamp(personagem.getMana() / (double) personagem.getManaMax()));

        vidaInimigo.setProgress(clamp(inimigo.getVida() / (double) inimigo.getVidaMax()));
        manaInimigo.setProgress(clamp(inimigo.getMana() / (double) inimigo.getManaMax()));
    }

    private double clamp(double valor) {
        if (valor < 0) return 0;
        if (valor > 1) return 1;
        return valor;
    }

    private boolean batalhaAcabou() {
        if (!inimigo.estaVivo()) {
            lblMensagem.setText("Vitória! Você derrotou o inimigo.");
            alternarBotoes(true);
            return true;
        }
        if (!personagem.estaVivo()) {
            lblMensagem.setText("Derrota! Seu personagem foi derrotado.");
            alternarBotoes(true);
            return true;
        }
        return false;
    }

    private void alternarBotoes(boolean desabilitar) {
        bntATA.setDisable(desabilitar);
        bntDEF.setDisable(desabilitar);
        bntHAB.setDisable(desabilitar);
    }

    private boolean validarEstado() {
        if (personagem == null || inimigo == null) {
            if (lblMensagem != null) {
                lblMensagem.setText("Erro ao carregar o estado da batalha.");
            }
            return false;
        }
        return true;
    }
}