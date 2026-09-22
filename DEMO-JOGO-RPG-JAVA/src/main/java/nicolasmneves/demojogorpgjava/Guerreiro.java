package nicolasmneves.demojogorpgjava;

public class Guerreiro extends Personagem {

    private static final int DEFESA_BASE = 10;
    private static final int VIDA_BASE = 100;
    private static final int ATAQUE_BASE = 10;
    private static final int MANA_BASE = 30;
    private static final int CUSTO_HABILIDADE = 5;

    public Guerreiro() {
        super(DEFESA_BASE, VIDA_BASE, ATAQUE_BASE, MANA_BASE);
    }

    @Override
    public void UsarHabilidade(Personagem alvo) {
        if (getMana() < CUSTO_HABILIDADE) {
            System.out.println("Mana insuficiente para usar Golpe Poderoso!");
            return;
        }
        setMana(getMana() - CUSTO_HABILIDADE);

        alvo.TomarDano(getAtaque() * 2);
    }
}