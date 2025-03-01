package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.infrastructure.persistence.GameSaveManager;
import br.com.braym.projetobitlifeclonev1.presentation.ConsoleUI;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Motor principal do jogo, gerenciando o loop de simulação e interações do
 * usuário.
 */
public class GameEngine {
	private static final Logger LOGGER = Logger.getLogger(GameEngine.class.getName());

	private Personagem personagem;
	private EventManager eventManager;
	private ConsoleUI ui;
	private Scanner scanner;

	public GameEngine(Personagem personagem, EventManager eventManager) {
		this.personagem = personagem;
		this.eventManager = eventManager;
		this.scanner = new Scanner(System.in);
		this.ui = new ConsoleUI(scanner);
	}

	public void run() {
		boolean running = true;
		while (running) {
			ui.mostrarMenu();
			int escolha = ui.lerOpcao();
			switch (escolha) {
			case 1:
				personagem.envelhecer();
				break;
			case 2:
				eventManager.processarEvento(personagem);
				break;
			case 3:
				ui.mostrarStatus(personagem);
				break;
			case 4:
				System.out.print("Digite o caminho para salvar o jogo: ");
				String caminhoSalvar = scanner.nextLine();
				GameSaveManager.saveGame(personagem, caminhoSalvar);
				break;
			case 5:
				System.out.print("Digite o caminho para carregar o jogo: ");
				String caminhoCarregar = scanner.nextLine();
				Personagem pCarregado = GameSaveManager.loadGame(caminhoCarregar);
				if (pCarregado != null) {
					personagem = pCarregado;
					System.out.println("Jogo carregado com sucesso.");
				}
				break;
			case 6:
				running = false;
				System.out.println("Encerrando o jogo...");
				break;
			default:
				System.out.println("Opção inválida. Tente novamente.");
				break;
			}
		}
	}
}
