package nicolasmneves.demojogorpgjava;

public abstract class Personagem {

    private int defesaBase;
    private int bonusDefesa;
    private int vida;
    private int vidaMax;
    private int ataque;
    private int mana;
    private int manaMax;
    private boolean posturaDefesa = false;

    public Personagem(int defesa, int vida, int ataque, int mana) {
        this.defesaBase = defesa;
        this.bonusDefesa = 0;
        this.vida = vida;
        this.vidaMax = vida;
        this.ataque = ataque;
        this.mana = mana;
        this.manaMax = mana;
    }

    //    -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

    public int getDefesa() {
        return defesaBase + bonusDefesa;
    }

    public void setDefesa(int defesa) {
        this.defesaBase = defesa;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {

        this.vida = Math.max(0, Math.min(vida, vidaMax));
    }

    public int getVidaMax() {
        return vidaMax;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, manaMax));
    }

    public int getManaMax() {
        return manaMax;
    }

    public boolean isPosturaDefesa() {
        return posturaDefesa;
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    //    -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

    public void Atacar(Personagem alvo) {
        alvo.TomarDano(this.ataque);
    }

    public void Defender() {

        if (!posturaDefesa) {
            bonusDefesa += 5;
            posturaDefesa = true;
        }
    }

    public void TomarDano(int danoBase) {
        int danoReal = danoBase - getDefesa();
        if (danoReal < 0) {
            danoReal = 0;
        }
        setVida(vida - danoReal);


        if (posturaDefesa) {
            bonusDefesa -= 5;
            posturaDefesa = false;
        }
    }

    public void UsarHabilidade(Personagem alvo) {

    }
}