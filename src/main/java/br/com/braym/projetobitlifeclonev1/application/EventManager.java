package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import br.com.braym.projetobitlifeclonev1.impl.EventoImpl;
import br.com.braym.projetobitlifeclonev1.utils.EventoJSONReader;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;
import br.com.braym.projetobitlifeclonev1.utils.RandomUtils;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Gerencia os eventos de acordo com a fase de vida do personagem, garantindo
 * que cada evento de uma fase só ocorra uma vez.
 */
public class EventManager {
	private static final Logger LOGGER = Logger.getLogger(EventManager.class.getName());

	// Mapa do "folder da fase" -> Lista de todos eventos possíveis
	private final Map<String, List<Evento>> eventosPorFase = new HashMap<>();

	// Mapa do "folder da fase" -> Conjunto de IDs dos eventos já usados
	private final Map<String, Set<String>> eventosProcessadosPorFase = new HashMap<>();

	// Pasta-base onde ficam os arquivos de eventos (ex.: "Eventos")
	private final String baseFolder;

	// Scanner para input do usuário
	private final Scanner scanner;

	public EventManager(String baseFolder) {
		this.baseFolder = baseFolder;
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Carrega (cache) os eventos para a fase de vida especificada, se ainda não
	 * carregado.
	 */
	private void carregarEventosSeNecessario(String faseFolder) {
		if (!eventosPorFase.containsKey(faseFolder)) {
			// Carrega do JSON
			String filePath = baseFolder + "/" + faseFolder + "/eventos.json";
			List<Evento> listaEventos = EventoJSONReader.lerEventos(filePath);

			if (listaEventos == null) {
				listaEventos = new ArrayList<>();
			}
			eventosPorFase.put(faseFolder, listaEventos);

			// Também inicializa o conjunto de eventos processados dessa fase
			eventosProcessadosPorFase.put(faseFolder, new HashSet<>());

			LOGGER.info("Carregados " + listaEventos.size() + " evento(s) para a fase " + faseFolder);
		}
	}

	/**
	 * Quando o personagem muda de faixa etária, limpamos o conjunto de eventos já
	 * usados para permitir que, se for a mesma fase recarregada, ele comece "do
	 * zero".
	 */
	public void reiniciarFase(String faseFolder) {
		// Limpa o conjunto de eventos usados para a fase
		if (eventosProcessadosPorFase.containsKey(faseFolder)) {
			eventosProcessadosPorFase.put(faseFolder, new HashSet<>());
			LOGGER.info("Fase " + faseFolder + " reiniciada para que eventos possam ocorrer novamente");
		}
	}

	/**
	 * Seleciona aleatoriamente um evento da fase atual do personagem (sem repetir).
	 * Retorna false se não houver mais eventos disponíveis para a fase.
	 */
	public boolean processarEvento(Personagem personagem) {
		String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());

		// Garante que temos a lista e o conjunto de usados cacheados
		carregarEventosSeNecessario(faseFolder);

		List<Evento> todosEventosDaFase = eventosPorFase.get(faseFolder);
		Set<String> usados = eventosProcessadosPorFase.get(faseFolder);

		// Filtra apenas os eventos que ainda não foram usados
		List<Evento> disponiveis = todosEventosDaFase.stream().filter(e -> !usados.contains(e.getId()))
				.collect(Collectors.toList());

		if (disponiveis.isEmpty()) {
			System.out.println("Não há mais eventos disponíveis para esta fase.");
			return false;
		}

		// Escolhe um evento aleatoriamente
		int indiceAleatorio = RandomUtils.gerarNumero(0, disponiveis.size() - 1);
		Evento eventoSelecionado = disponiveis.get(indiceAleatorio);

		System.out.println("Evento selecionado: " + eventoSelecionado.getDescricao());

		// Criar uma instância de EventoImpl para executar o evento
		if (eventoSelecionado instanceof EventoInterface) {
			((EventoInterface) eventoSelecionado).executarEvento(personagem);
		} else {
			// Criar uma implementação temporária do evento
			EventoImpl eventoImpl = new EventoImpl(eventoSelecionado.getDescricao(), eventoSelecionado.getOpcoes(),
					eventoSelecionado.getEfeitos(), eventoSelecionado.getAtributo(), scanner);
			eventoImpl.setEfeitosMultiplos(eventoSelecionado.getEfeitosMultiplos());
			eventoImpl.executarEvento(personagem);
		}

		// Marca esse evento como usado, para não cair de novo
		usados.add(eventoSelecionado.getId());
		LOGGER.info("Evento " + eventoSelecionado.getId() + " marcado como usado");

		return true;
	}

	/**
	 * Retorna o número de eventos disponíveis para a fase atual do personagem.
	 */
	public int getQuantidadeEventosDisponiveis(Personagem personagem) {
		String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());

		// Garante que temos a lista e o conjunto de usados cacheados
		carregarEventosSeNecessario(faseFolder);

		List<Evento> todosEventosDaFase = eventosPorFase.get(faseFolder);
		Set<String> usados = eventosProcessadosPorFase.get(faseFolder);

		// Filtra apenas os eventos que ainda não foram usados
		List<Evento> disponiveis = todosEventosDaFase.stream().filter(e -> !usados.contains(e.getId()))
				.collect(Collectors.toList());

		return disponiveis.size();
	}

	/**
	 * Verifica se uma nova fase foi atingida pelo personagem e atualiza o estado
	 * interno. Deve ser chamado sempre que o personagem envelhecer.
	 */
	public void verificarMudancaFase(Personagem personagem, int idadeAnterior) {
		String faseFolderAnterior = FaseDaVidaResolver.getFaseDaVidaFolder(idadeAnterior);
		String faseFolderAtual = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());

		if (!faseFolderAnterior.equals(faseFolderAtual)) {
			// Personagem mudou de fase, então carregamos a nova fase
			carregarEventosSeNecessario(faseFolderAtual);
			LOGGER.info("Personagem mudou de fase: " + faseFolderAnterior + " -> " + faseFolderAtual);
		}
	}
}