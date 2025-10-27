# Projeto Banco Digital (Simulação)

Este é um projeto de simulação de um banco digital desenvolvido em Java, focado em demonstrar conceitos de Programação Orientada a Objetos (POO), manipulação de coleções, e um modelo de auditoria de transações.

O sistema é executado via console (CLI) e permite aos usuários criar contas, realizar depósitos, saques, transferências via Pix, e gerenciar uma carteira de investimentos.

## Funcionalidades

O aplicativo de console oferece as seguintes operações:

1.  **Criar uma conta**: Cria uma nova `AccountWallet` com chaves Pix e um depósito inicial.
2.  **Criar um investimento**: Define um novo tipo de produto de investimento (`Investment`) com uma taxa e um valor inicial.
3.  **Criar uma carteira de investimento**: Vincula uma `AccountWallet` a um `Investment` para começar a investir.
4.  **Depositar na conta**: Adiciona fundos a uma conta existente via chave Pix.
5.  **Sacar da conta**: Retira fundos de uma conta existente via chave Pix.
6.  **Transferência entre contas**: Transfere fundos de uma conta (origem) para outra (destino).
7.  **Investir**: Transfere fundos da `AccountWallet` para a `InvestmentWallet` associada.
8.  **Sacar investimento**: Resgata fundos da `InvestmentWallet` de volta para a `AccountWallet`.
9.  **Listar contas**: Exibe todas as `AccountWallet` criadas.
10. **Listar investimentos**: Exibe todos os produtos de `Investment` disponíveis.
11. **Listar carteiras de investimento**: Exibe todas as `InvestmentWallet` ativas.
12. **Atualizar investimentos**: Aplica a taxa de rendimento a todas as carteiras de investimento ativas.
13. **Histórico de conta**: Exibe o histórico de transações de uma conta específica.
14. **Sair**: Encerra a aplicação.

## Conceitos Principais do Modelo

Este projeto utiliza um modelo de dados interessante para gerenciar fundos e auditoria.

### 1. Modelo de "Wallet" (Carteira)
O sistema é baseado em uma classe abstrata `Wallet`. Existem duas implementações principais:
* `AccountWallet`: Representa a conta corrente do cliente. Ela armazena as chaves Pix e os fundos da conta.
* `InvestmentWallet`: Representa a carteira de investimentos do cliente. Ela vincula uma `AccountWallet` a um produto de `Investment` e armazena os fundos investidos.

### 2. Modelo de "Money" (Dinheiro) e Auditoria
O saldo de uma `Wallet` não é um simples valor numérico (como `long` ou `double`). Em vez disso, o saldo é representado por uma `List<Money>`. O valor total do saldo é, portanto, o tamanho dessa lista (`list.size()`).

* **`Money`**: Cada objeto `Money` representa uma unidade monetária (neste caso, um centavo, já que os valores são tratados como `long`).
* **`MoneyAudit`**: Cada objeto `Money` contém um histórico de todas as transações pelas quais passou, armazenado em uma `List<MoneyAudit>`.

Esse modelo permite uma **auditoria e rastreabilidade completas** de cada unidade monetária no sistema. Quando uma transferência ocorre, os objetos `Money` são removidos da lista da carteira de origem e adicionados à lista da carteira de destino, e um novo registro `MoneyAudit` é adicionado a cada objeto `Money` transferido.

## Estrutura do Projeto

O projeto está organizado nos seguintes pacotes:

```
br.com.dio
├── Main.java               // Ponto de entrada da aplicação (CLI)
|
├── exception/              // Classes de exceção customizadas
│   ├── AccountNotFoundException.java
│   ├── AccountWithInvestmentException.java
│   ├── InvestmentNotFoundException.java
│   ├── NoFundsEnoughException.java
│   └── PixInUseException.java
│   └── WalletNotFoundException.java
|
├── model/                  // Classes de domínio e entidades
│   ├── AccountWallet.java
│   ├── BankService.java    // Enum (ACCOUNT, INVESTMENT)
│   ├── Investment.java     // Record (produto de investimento)
│   ├── InvestmentWallet.java
│   ├── Money.java          // Unidade monetária com histórico
│   ├── MoneyAudit.java     // Record (registro de transação)
│   └── Wallet.java         // Classe abstrata base
|
└── repository/             // Classes de acesso e gerenciamento de dados (em memória)
    ├── AccountRepository.java
    ├── CommonsRepository.java
    └── InvestmentRepository.java
```

## Classes Principais

* **`Main`**: Contém o método `main` que executa o loop do menu do console. Ele interage com os repositórios para executar as operações solicitadas pelo usuário.
* **`AccountRepository`**: Gerencia o "banco de dados" em memória de `AccountWallet`. Controla a criação de contas, depósitos, saques e transferências.
* **`InvestmentRepository`**: Gerencia os produtos de `Investment` e as carteiras `InvestmentWallet`. Controla a criação de investimentos, depósitos, resgates e a atualização dos rendimentos.
* **`Wallet` (Abstrata)**: Define o comportamento comum de uma carteira, incluindo `getFunds()`, `addMoney()`, e `reduceMoney()`.
* **`Money` e `MoneyAudit`**: O núcleo do sistema de fundos. `MoneyAudit` é um `record` que armazena os detalhes da transação (ID, serviço, descrição, data/hora).

## Dependências

Este projeto utiliza a biblioteca **Lombok** para reduzir código boilerplate (como getters, toString, etc.).

* `org.projectlombok:lombok`

Certifique-se de que o Lombok esteja configurado corretamente em sua IDE (por exemplo, habilitando o processamento de anotações) e incluído no classpath durante a compilação.

## Como Executar

1.  **Pré-requisitos**:
    * Java JDK (versão 17+ para `record`)
    * Lombok (biblioteca `.jar`)

2.  **Compilação (via linha de comando)**:
    Coloque o `lombok.jar` no mesmo diretório dos seus arquivos `.java` ou adicione-o ao seu classpath.

    ```bash
    # Navegue até o diretório raiz dos pacotes (onde a pasta 'br' está)
    # Exemplo para compilar com Lombok no classpath
    javac -cp .:lombok.jar br/com/dio/*.java br/com/dio/exception/*.java br/com/dio/model/*.java br/com/dio/repository/*.java
    ```
    *Nota: É altamente recomendado usar uma ferramenta de build como Maven ou Gradle, que gerencia o Lombok automaticamente.*

3.  **Execução**:
    Após a compilação, execute a classe `Main`.

    ```bash
    # A partir do mesmo diretório raiz
    java -cp .:lombok.jar br.com.dio.Main
    ```

4.  **Interação**:
    Siga as instruções no console para interagir com o banco digital.

    ```
    Olá seja bem vindo ao Banco Digital
    Selecione a operação desejada
    1 - Criar uma conta
    2 - Criar um investimento
    3 - Criar uma carteira de investimento
    ...
    14 - Sair
    ```
