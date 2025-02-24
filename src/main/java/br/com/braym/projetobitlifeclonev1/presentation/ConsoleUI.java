package br.com.braym.projetobitlifeclonev1.presentation;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import java.util.Scanner;

/**
 * Interface de usuário baseada em console para interação com o jogo.
 */
public class ConsoleUI {
    private Scanner scanner;

    public ConsoleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Exibe o menu principal.
     */
    public void mostrarMenu() {
        System.out.println("==== BitLife Clone ====");
        System.out.println("1. Envelhecer personagem");
        System.out.println("2. Processar evento aleatório");
        System.out.println("3. Mostrar status do personagem");
        System.out.println("4. Salvar jogo");
        System.out.println("5. Carregar jogo");
        System.out.println("6. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Lê a opção escolhida pelo usuário.
     * @return a opção escolhida
     */
    public int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Exibe o status atual do personagem.
     * @param personagem o personagem cujos atributos serão exibidos
     */
    public void mostrarStatus(Personagem personagem) {
        System.out.println("==== Status do Personagem ====");
        System.out.println("Nome: " + personagem.getNome());
        System.out.println("Idade: " + personagem.getIdade());
        System.out.println("Estado de Vida: " + personagem.getEstadoVida().getEstado());
        System.out.println("Saúde: " + personagem.getSaude());
        System.out.println("Sanidade: " + personagem.getSanidade());
        System.out.println("Felicidade: " + personagem.getFelicidade());
        System.out.println("Inteligência: " + personagem.getInteligencia());
        System.out.println("Carisma: " + personagem.getCarisma());
        System.out.println("Finanças: " + personagem.getFinancas());
        System.out.println("=============================");
    }
}
