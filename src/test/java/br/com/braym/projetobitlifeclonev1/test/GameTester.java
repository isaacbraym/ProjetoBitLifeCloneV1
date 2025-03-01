package br.com.braym.projetobitlifeclonev1.test;

import br.com.braym.projetobitlifeclonev1.application.EventManager;
import br.com.braym.projetobitlifeclonev1.impl.ConsoleObservador;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;

import java.util.Scanner;

/**
 * Classe de teste para o jogo BitLife Clone com controle adequado de eventos.
 */
public class GameTester {
	private static EventManager gerenciadorEventos;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Personagem personagem = new Personagem("Braym");
		personagem.adicionarObservador(new ConsoleObservador());

		gerenciadorEventos = new EventManager("Eventos");

		System.out.println("Personagem criado: " + personagem.getNome());
		System.out.println("Idade inicial: " + personagem.getIdade());

		boolean running = true;
		while (running) {
			exibirMenu();
			int opcao = lerOpcao(scanner);
			switch (opcao) {
			case 1:
				mostrarStatus(personagem);
				break;
			case 2:
				personagem.envelhecer();
				System.out.println("Idade após envelhecer: " + personagem.getIdade());
				break;
			case 3:
				gerenciadorEventos.processarEvento(personagem);
				break;
			case 4:
				running = false;
				System.out.println("Encerrando o jogo...");
				break;
			default:
				System.out.println("Opção inválida. Tente novamente.");
			}
		}
		scanner.close();
	}

	private static void exibirMenu() {
		System.out.println("\n==== BitLife Clone ====");
		System.out.println("1. Mostrar status do personagem");
		System.out.println("2. Envelhecer personagem");
		System.out.println("3. Processar evento aleatório");
		System.out.println("4. Sair");
		System.out.print("Escolha uma opção: ");
	}

	private static int lerOpcao(Scanner scanner) {
		int opcao = -1;
		try {
			opcao = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			// valor inválido
		}
		return opcao;
	}

	private static void mostrarStatus(Personagem personagem) {
		System.out.println("\n==== Status do Personagem ====");
		System.out.println(String.format(
				"Nome: %s | Idade: %d | Aparência: %d | Saúde: %d | Sanidade: %d | Felicidade: %d | Inteligência: %d | Carisma: %d | Finanças: %d",
				personagem.getNome(), personagem.getIdade(), personagem.getAparencia(), personagem.getSaude(),
				personagem.getSanidade(), personagem.getFelicidade(), personagem.getInteligencia(),
				personagem.getCarisma(), personagem.getFinancas()));
	}

}
