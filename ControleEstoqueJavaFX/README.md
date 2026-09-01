# Controle de Estoque com POO e MySQL — JavaFX

Conversão da Atividade 06 (originalmente Windows Forms/C#) para **Java + JavaFX + MySQL**,
mantendo as mesmas regras de negócio: o estoque nunca é editado diretamente no cadastro do
produto — todo produto nasce com saldo zero e qualquer variação ocorre exclusivamente por
uma **movimentação** (entrada ou saída), registrada em uma transação com commit/rollback.

## Estrutura do projeto

```
ControleEstoqueJavaFX
├── pom.xml
└── src/main
    ├── java/com/controleestoque
    │   ├── app/MainApp.java              -> ponto de entrada da aplicação
    │   ├── model/
    │   │   ├── Categoria.java
    │   │   ├── Produto.java
    │   │   └── Movimentacao.java
    │   ├── data/
    │   │   ├── Conexao.java              -> conexão JDBC com o MySQL
    │   │   ├── CategoriaDAO.java         -> Inserir, Atualizar, Excluir, Listar
    │   │   ├── ProdutoDAO.java           -> Inserir, Atualizar, AlterarSituacao, Excluir,
    │   │   │                                 BuscarPorId, Listar, Pesquisar, ListarEstoqueBaixo
    │   │   ├── MovimentacaoDAO.java      -> Registrar (transação), Listar, ListarPorProduto,
    │   │   │                                 ListarPorPeriodo, ListarUltimas
    │   │   └── EstoqueException.java     -> exceção de regra de negócio
    │   └── controller/
    │       ├── PrincipalController.java  -> painel com indicadores e atalhos
    │       ├── CategoriasController.java
    │       ├── ProdutosController.java
    │       └── MovimentacoesController.java
    └── resources
        ├── fxml/                         -> telas (principal, categorias, produtos, movimentacoes)
        ├── css/style.css
        └── sql/db_controle_estoque.sql   -> script completo do banco
```

## Pré-requisitos

- JDK 17+
- Maven 3.9+
- MySQL Server 8+ em execução

## 1. Criar o banco de dados

Execute o script `src/main/resources/sql/db_controle_estoque.sql` no MySQL
(Workbench, DBeaver, ou linha de comando):

```bash
mysql -u root -p < src/main/resources/sql/db_controle_estoque.sql
```

## 2. Configurar a conexão

Ajuste usuário/senha (e host/porta, se necessário) em
`src/main/java/com/controleestoque/data/Conexao.java`:

```java
private static final String USUARIO = "root";
private static final String SENHA = "root";
```

## 3. Executar a aplicação

```bash
mvn clean javafx:run
```

Ou gerar um JAR executável com todas as dependências:

```bash
mvn clean package
java -jar target/controle-estoque-javafx-jar-with-dependencies.jar
```

## Funcionalidades implementadas

- **Categorias**: cadastrar, editar, listar e excluir.
- **Produtos**: cadastrar, editar, pesquisar (por código/nome), ativar/desativar.
  O saldo (`estoque`) é somente leitura no formulário — só muda via movimentação.
- **Movimentações**: registrar ENTRADA ou SAÍDA dentro de uma transação JDBC
  (`Connection.setAutoCommit(false)` + `commit()`/`rollback()`), validando:
  - produto e tipo obrigatórios;
  - quantidade maior que zero;
  - impedimento de saída superior ao estoque disponível;
  - impedimento de movimentação em produto desativado.
  Também permite consultar o histórico completo, filtrar por produto e por período.
- **Painel principal**: total de produtos, produtos ativos, itens em estoque,
  produtos com estoque baixo, valor estimado do estoque (preço de compra × estoque) e
  últimas movimentações — com destaque visual para itens abaixo do estoque mínimo.

## Regras de negócio (mesmas da especificação original)

- Código do produto obrigatório e único; nome e categoria obrigatórios.
- Preço de compra não pode ser negativo; preço de venda deve ser maior que zero.
- Estoque inicial sempre zero; só é alterado por movimentações.
- Produtos desativados não recebem novas movimentações até serem reativados.
- `NovoEstoque = EstoqueAtual + Quantidade` (ENTRADA) /
  `NovoEstoque = EstoqueAtual - Quantidade` (SAÍDA, com validação de saldo suficiente).
