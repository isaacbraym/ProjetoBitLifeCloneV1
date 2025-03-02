package br.com.braym.projetobitlifeclonev1.service;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serviço responsável por encapsular a entrada do usuário,
 * fornecendo métodos padronizados para leitura de dados.
 */
public class ProvedorEntrada implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ProvedorEntrada.class.getName());
    private final Scanner scanner;
    
    /**
     * Construtor padrão que inicializa com System.in
     */
    public ProvedorEntrada() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Construtor que permite injetar um scanner
     * @param scanner Scanner a ser utilizado
     */
    public ProvedorEntrada(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Obtém o scanner utilizado por este provedor
     * @return O scanner atual
     */
    public Scanner obterScanner() {
        return scanner;
    }
    
    /**
     * Lê um número inteiro da entrada
     * @param mensagem Mensagem de prompt para o usuário
     * @return O número inteiro lido
     */
    public int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            return lerInteiro(mensagem);
        }
    }
    
    /**
     * Lê um número inteiro com validação de intervalo
     * @param mensagem Mensagem de prompt para o usuário
     * @param minimo Valor mínimo aceitável
     * @param maximo Valor máximo aceitável
     * @return O número inteiro validado
     */
    public int lerInteiroComIntervalo(String mensagem, int minimo, int maximo) {
        int valor;
        do {
            valor = lerInteiro(mensagem);
            if (valor < minimo || valor > maximo) {
                System.out.printf("Valor deve estar entre %d e %d. Tente novamente.\n", minimo, maximo);
            }
        } while (valor < minimo || valor > maximo);
        return valor;
    }
    
    /**
     * Lê uma linha de texto da entrada
     * @param mensagem Mensagem de prompt para o usuário
     * @return A linha de texto lida
     */
    public String lerLinha(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
    
    /**
     * Lê uma linha de texto com validação de não estar vazia
     * @param mensagem Mensagem de prompt para o usuário
     * @return A linha de texto não vazia
     */
    public String lerLinhaObrigatoria(String mensagem) {
        String linha;
        do {
            linha = lerLinha(mensagem);
            if (linha.trim().isEmpty()) {
                System.out.println("Este campo não pode estar vazio. Tente novamente.");
            }
        } while (linha.trim().isEmpty());
        return linha;
    }
    
    /**
     * Fecha o scanner quando não for mais necessário
     */
    @Override
    public void close() {
        try {
            scanner.close();
            LOGGER.info("Scanner fechado com sucesso.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao fechar scanner: " + e.getMessage(), e);
        }
    }
}