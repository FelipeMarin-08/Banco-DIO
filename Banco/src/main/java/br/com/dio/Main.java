package br.com.dio;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.NoFundsEnoughException;
import br.com.dio.exception.WalletNotFoundException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.MoneyAudit;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;

public class Main {
    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.println("Olá seja bem vindo ao Banco Digital");
        while(true) {
            System.out.println("Selecione a operação desejada");
            System.out.println("1 - Criar uma conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Criar uma carteira de investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - sacar da conta");
            System.out.println("6 - Transferencia entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimento");
            System.out.println("12 - Atualizar investimentos");
            System.out.println("13 - Historico de conta");
            System.out.println("14 - Sair");
            int op = sc.nextInt();
            switch (op){
                case 1: createAccount();
                    break;
                case 2: createInvestment();
                    break;
                case 3: createWalletInvestment();
                    break;
                case 4: deposit();
                    break;
                case 5: withdraw();
                    break;
                case 6: transferToAccount();
                    break;
                case 7: incInvestment();
                    break;
                case 8: rescueInvestment();
                    break;
                case 9: accountRepository.list().forEach(System.out::println);
                    break;
                case 10: investmentRepository.list().forEach(System.out::println);
                    break;
                case 11: investmentRepository.listWallets().forEach(System.out::println);
                    break;
                case 12:{
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos reajustados");
                }
                    break;
                case 13: checkHistory();
                    break;
                case 14: System.exit(0);
                    break;
                default: System.out.println("Opção inválida");
                    break;
            }

        }
    }
    private static void createAccount() {
        System.out.println("Informe as chaves pix (separadas por ';'");

        var pix = Arrays.stream(sc.next().split(";")).toList();
        System.out.println("Informe o valor inicial de deposito");
        var amount = sc.nextLong();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("Conta criada com sucesso"+ wallet);
    }

    private static void createInvestment() {
        System.out.println("Informe a taxa do investimento");
        var tax = sc.nextInt();
        System.out.println("Informe o valor inicial de deposito");
        var initialFunds = sc.nextLong();
        var investiment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investimento criado com sucesso"+ investiment);
    }

    private static void withdraw() {
        System.out.println("Informe a chave pix da conta para saque:");
        var pix = sc.next();
        System.out.println("Informe o valor a ser sacado:");
        var amount = sc.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
        } catch(NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    private static void deposit() {
        System.out.println("Informe a chave pix da conta para deposito:");
        var pix = sc.next();
        System.out.println("Informe o valor a ser depositado:");
        var amount = sc.nextLong();
        try {
            accountRepository.deposit(pix, amount);
        } catch(AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    private static void transferToAccount() {
        System.out.println("Informe a chave pix da conta de origem:");
        var source = sc.next();
        System.out.println("Informe a chave pix da conta de destino:");
        var target = sc.next();
        System.out.println("Informe o valor a ser transferido:");
        var amount = sc.nextLong();
        try {
            accountRepository.transferMoney(source, target, amount);
        }catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void createWalletInvestment() {
        System.out.println("Informe a chave pix da conta:");
        var pix = sc.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe o id do investimento:");
        var investmentId = sc.nextInt();
        var investmentWallet = investmentRepository.initInvestment(account, investmentId);
        System.out.println("Carteira de investimento criada com sucesso: "+ investmentWallet);
    }

    private static void incInvestment() {
        System.out.println("Informe a chave pix da conta para investimento:");
        var pix = sc.next();
        System.out.println("Informe o valor a ser investido:");
        var amount = sc.nextLong();
        try {
            investmentRepository.deposit(pix, amount);
        } catch(WalletNotFoundException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void rescueInvestment() {
        System.out.println("Informe a chave pix da conta para resgate do investimento:");

        var pix = sc.next();
        System.out.println("Informe o valor a ser sacado:");
        var amount = sc.nextLong();
        try {
            investmentRepository.withdraw(pix, amount);
        } catch(NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    private static void checkHistory() {
        System.out.println("Informe a chave pix da conta:");
        var pix = sc.next();
        AccountWallet wallet;
        try {
           var sortedHistory = accountRepository.getHistory(pix);
            sortedHistory.forEach((k,v) -> {
                System.out.println(k.format(ISO_DATE_TIME));
                System.out.println(v.getFirst().transactionId());
                System.out.println(v.getFirst().description());
                System.out.println("R$ "+(v.size() /100)+","+(v.size()%100));


            });
        } catch(AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }
}
