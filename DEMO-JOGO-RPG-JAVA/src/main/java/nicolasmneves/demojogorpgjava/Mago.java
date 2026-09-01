package nicolasmneves.demojogorpgjava;

public class Mago extends Personagem {

    private static final int DEFESA_BASE = 5;
    private static final int VIDA_BASE = 80;
    private static final int ATAQUE_BASE = 8;
    private static final int MANA_BASE = 50;
    private static final int CUSTO_HABILIDADE = 8;

    public Mago() {
        super(DEFESA_BASE, VIDA_BASE, ATAQUE_BASE, MANA_BASE);
    }

    @Override
    public void UsarHabilidade(Personagem alvo) {
        if (getMana() < CUSTO_HABILIDADE) {
            System.out.println("Mana insuficiente para usar Bola de Fogo!");
            return;
        }
        setMana(getMana() - CUSTO_HABILIDADE);

        int dano = (getAtaque() * 3) - (alvo.getDefesa() / 2);
        alvo.TomarDano(Math.max(dano, 0));
    }
}