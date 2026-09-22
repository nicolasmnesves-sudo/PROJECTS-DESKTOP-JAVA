package nicolasmneves.demojogorpgjava;

public class Inimigos extends Personagem {

    private static final int DEFESA_BASE = 3;
    private static final int VIDA_BASE = 100;
    private static final int ATAQUE_BASE = 10;
    private static final int MANA_BASE = 10;

    public Inimigos() {
        super(DEFESA_BASE, VIDA_BASE, ATAQUE_BASE, MANA_BASE);
    }

    @Override
    public void UsarHabilidade(Personagem alvo) {

        alvo.TomarDano((int) (getAtaque() * 1.5));
    }
}