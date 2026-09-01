package view;

// ==========================================
// 1. SUPERCLASSE (CLASSE BASE)
// ==========================================
public class Funcionario {
    // Atributos encapsulados (private)
    private String nome;
    private double salarioBase;

    // Construtor Parametrizado
    public Funcionario(String nome, double salarioBase) {
        this.nome = nome;
        this.salarioBase = salarioBase;
    }

    // Getters e Setters para proteção dos dados
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    // Método que será sobrescrito (Polimorfismo)
    public double calcularSalario() {
        return this.salarioBase;
    }
}

// ==========================================
// 2. SUBCLASSE GERENTE (HERANÇA)
// ==========================================
class Gerente extends Funcionario {
    private double bonus;

    // Uso do 'super' para invocar o construtor da Superclasse
    public Gerente(String nome, double salarioBase, double bonus) {
        super(nome, salarioBase); 
        this.bonus = bonus;
    }

    // Sobrescrita do método utilizando Polimorfismo
    @Override
    public double calcularSalario() {
        // Usa o salário da classe base e soma o bônus específico do Gerente
        return super.getSalarioBase() + this.bonus;
    }
}

// ==========================================
// 3. SUBCLASSE DESENVOLVEDOR (HERANÇA)
// ==========================================
class Desenvolvedor extends Funcionario {
    private int horasExtras;

    public Desenvolvedor(String nome, double salarioBase, int horasExtras) {
        super(nome, salarioBase);
        this.horasExtras = horasExtras;
    }

    // Sobrescrita do método utilizando Polimorfismo
    @Override
    public double calcularSalario() {
        // Cada hora extra adiciona R$ 50,00 ao salário base
        return super.getSalarioBase() + (this.horasExtras * 50.0);
    }
}

// ==========================================
// 4. EXECUÇÃO DO SISTEMA
// ==========================================
class SistemaMain {
    public static void main(String[] args) {
        // Criando objetos usando polimorfismo (Tipo Funcionario guardando subclasses)
        Funcionario func1 = new Gerente("Alice", 5000.0, 1500.0);
        Funcionario func2 = new Desenvolvedor("Bruno", 4000.0, 10);

        // O mesmo método se comporta de formas diferentes dependendo do objeto real
        System.out.println("Funcionario: " + func1.getNome() + " | Salário Total: R$ " + func1.calcularSalario());
        System.out.println("Funcionario: " + func2.getNome() + " | Salário Total: R$ " + func2.calcularSalario());
    }
}