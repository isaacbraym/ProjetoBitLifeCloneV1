package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.infrastructure.persistence.GerenciadorSalvamentoJogo;
import br.com.braym.projetobitlifeclonev1.infrastructure.persistence.GerenciadorSalvamentoJogo.DadosJogoSalvo;
import br.com.braym.projetobitlifeclonev1.presentation.InterfaceConsole;
import br.com.braym.projetobitlifeclonev1.service.GerenciadorEventos;
import br.com.braym.projetobitlifeclonev1.service.ProvedorEntrada;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Motor principal do jogo, gerenciando o loop de simulação e interações do
 * usuário. Utiliza o padrão de injeção de dependências para maior flexibilidade
 * e testabilidade.
 */
public class MotorJogo {
	private static final Logger LOGGER = Logger.getLogger(MotorJogo.class.getName());

	private Personagem personagem;
	private final GerenciadorEventos gerenciadorEventos;
	private final InterfaceConsole interfaceConsole;
	private final ProvedorEntrada provedorEntrada;
	private final GerenciadorSalvamentoJogo gerenciadorSalvamento;

	/**
	 * Construtor com injeção de dependências
	 * 
	 * @param personagem            Personagem inicial do jogo
	 * @param gerenciadorEventos    Gerenciador de eventos do jogo
	 * @param interfaceConsole      Interface de usuário
	 * @param provedorEntrada       Provedor de entrada do usuário
	 * @param gerenciadorSalvamento Gerenciador de salvamento/carregamento
	 */
	public MotorJogo(Personagem personagem, GerenciadorEventos gerenciadorEventos, InterfaceConsole interfaceConsole,
			ProvedorEntrada provedorEntrada, GerenciadorSalvamentoJogo gerenciadorSalvamento) {
		this.personagem = personagem;
		this.gerenciadorEventos = gerenciadorEventos;
		this.interfaceConsole = interfaceConsole;
		this.provedorEntrada = provedorEntrada;
		this.gerenciadorSalvamento = gerenciadorSalvamento;
	}

	/**
	 * Inicia o loop principal do jogo
	 */
	public void executar() {
		boolean executando = true;

		while (executando) {
			try {
				executando = processarCicloDeJogo();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Erro durante execução do jogo: " + e.getMessage(), e);
				System.out.println("Ocorreu um erro: " + e.getMessage());
				System.out.println("O jogo continuará, mas alguns dados podem ter sido perdidos.");
			}
		}
	}

	/**
	 * Processa um ciclo completo do jogo (exibição de menu, processamento de
	 * escolha)
	 * 
	 * @return true se o jogo deve continuar, false se deve encerrar
	 */
	private boolean processarCicloDeJogo() {
		interfaceConsole.mostrarMenu();
		int escolha = interfaceConsole.lerOpcao();

		return switch (escolha) {
		case 1 -> processarEnvelhecimento();
		case 2 -> processarEvento();
		case 3 -> mostrarStatus();
		case 4 -> salvarJogo();
		case 5 -> carregarJogo();
		case 6 -> encerrarJogo();
		default -> processarOpcaoInvalida();
		};
	}

	/**
	 * Processa o envelhecimento do personagem
	 * 
	 * @return true para continuar o jogo
	 */
	private boolean processarEnvelhecimento() {
		int idadeAnterior = personagem.envelhecer();
		gerenciadorEventos.verificarMudancaFase(personagem, idadeAnterior);
		return true;
	}

	/**
	 * Processa um evento aleatório
	 * 
	 * @return true para continuar o jogo
	 */
	private boolean processarEvento() {
		boolean sucesso = gerenciadorEventos.processarEvento(personagem);
		if (!sucesso) {
			System.out.println("Tente envelhecer para ter acesso a novos eventos.");
		}
		return true;
	}

	/**
	 * Exibe o status atual do personagem
	 * 
	 * @return true para continuar o jogo
	 */
	private boolean mostrarStatus() {
		interfaceConsole.mostrarStatus(personagem);
		return true;
	}

	/**
	 * Salva o jogo atual
	 * 
	 * @return true para continuar o jogo
	 */
	private boolean salvarJogo() {
		boolean sucesso = gerenciadorSalvamento.salvarJogo(personagem);
		if (sucesso) {
			System.out.println("Jogo salvo com sucesso!");
		} else {
			System.out.println("Falha ao salvar o jogo. Verifique os logs para mais detalhes.");
		}
		return true;
	}

	/**
	 * Carrega um jogo salvo
	 * 
	 * @return true para continuar o jogo
	 */
	private boolean carregarJogo() {
		List<DadosJogoSalvo> jogosSalvos = gerenciadorSalvamento.listarJogosSalvos();

		if (jogosSalvos.isEmpty()) {
			System.out.println("Não há jogos salvos disponíveis.");
			return true;
		}

		System.out.println("\nDigite qual jogo você quer carregar:");
		for (int i = 0; i < jogosSalvos.size(); i++) {
			DadosJogoSalvo jogo = jogosSalvos.get(i);
			System.out.printf("%d - %s_%d_%s\n", i + 1, jogo.getNomePersonagem(), jogo.getIdade(),
					jogo.getDataSalvamento());
		}

		int escolha = provedorEntrada.lerInteiroComIntervalo("Escolha: ", 1, jogosSalvos.size());
		String caminhoArquivo = jogosSalvos.get(escolha - 1).getCaminhoArquivo();

		Personagem personagemCarregado = gerenciadorSalvamento.carregarJogo(caminhoArquivo);

		if (personagemCarregado != null) {
			this.personagem = personagemCarregado;
			System.out.println("Jogo carregado com sucesso!");
		} else {
			System.out.println("Falha ao carregar o jogo. O arquivo pode estar corrompido.");
		}

		return true;
	}

	/**
	 * Encerra o jogo
	 * 
	 * @return false para interromper o loop do jogo
	 */
	private boolean encerrarJogo() {
		System.out.println("Encerrando o jogo...");
		return false;
	}

	/**
	 * Processa opção inválida do menu
	 * 
	 * @return true para continuar o jogo
	 */
	private boolean processarOpcaoInvalida() {
		System.out.println("Opção inválida. Tente novamente.");
		return true;
	}
}