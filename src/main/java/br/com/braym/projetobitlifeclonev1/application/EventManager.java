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

	private final Map<String, List<Evento>> eventosPorFase = new HashMap<>();
	private final Map<String, Set<String>> eventosProcessadosPorFase = new HashMap<>();
	private final String baseFolder;
	private final Scanner scanner;

	public EventManager(String baseFolder) {
		this.baseFolder = baseFolder;
		this.scanner = new Scanner(System.in);
	}

	private void carregarEventosSeNecessario(String faseFolder) {
		if (!eventosPorFase.containsKey(faseFolder)) {
			String filePath = baseFolder + "/" + faseFolder + "/eventos.json";
			List<Evento> listaEventos = EventoJSONReader.lerEventos(filePath);
			eventosPorFase.put(faseFolder, listaEventos != null ? listaEventos : new ArrayList<>());
			eventosProcessadosPorFase.put(faseFolder, new HashSet<>());
			LOGGER.info("Carregados " + listaEventos.size() + " evento(s) para a fase " + faseFolder);
		}
	}

	public void reiniciarFase(String faseFolder) {
		if (eventosProcessadosPorFase.containsKey(faseFolder)) {
			eventosProcessadosPorFase.put(faseFolder, new HashSet<>());
			LOGGER.info("Fase " + faseFolder + " reiniciada.");
		}
	}

	public boolean processarEvento(Personagem personagem) {
		String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
		carregarEventosSeNecessario(faseFolder);

		List<Evento> todosEventosDaFase = eventosPorFase.get(faseFolder);
		Set<String> usados = eventosProcessadosPorFase.get(faseFolder);
		List<Evento> disponiveis = todosEventosDaFase.stream().filter(e -> !usados.contains(e.getId()))
				.collect(Collectors.toList());

		if (disponiveis.isEmpty()) {
			System.out.println("Não há mais eventos disponíveis para esta fase.");
			return false;
		}

		Evento eventoSelecionado = disponiveis.get(RandomUtils.gerarNumero(0, disponiveis.size() - 1));
		System.out.println("Evento selecionado: " + eventoSelecionado.getDescricao());

		if (eventoSelecionado instanceof EventoInterface) {
			((EventoInterface) eventoSelecionado).executarEvento(personagem);
		} else {
			new EventoImpl(eventoSelecionado.getDescricao(), eventoSelecionado.getOpcoes(),
					eventoSelecionado.getEfeitos(), eventoSelecionado.getAtributo(), scanner)
					.executarEvento(personagem);
		}

		usados.add(eventoSelecionado.getId());
		LOGGER.info("Evento " + eventoSelecionado.getId() + " marcado como usado.");
		return true;
	}

	public int getQuantidadeEventosDisponiveis(Personagem personagem) {
		String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
		carregarEventosSeNecessario(faseFolder);
		List<Evento> todosEventosDaFase = eventosPorFase.get(faseFolder);
		Set<String> usados = eventosProcessadosPorFase.get(faseFolder);
		List<Evento> disponiveis = todosEventosDaFase.stream().filter(e -> !usados.contains(e.getId()))
				.collect(Collectors.toList());
		return disponiveis.size();
	}

	public void verificarMudancaFase(Personagem personagem, int idadeAnterior) {
		String faseFolderAnterior = FaseDaVidaResolver.getFaseDaVidaFolder(idadeAnterior);
		String faseFolderAtual = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());

		if (!faseFolderAnterior.equals(faseFolderAtual)) {
			carregarEventosSeNecessario(faseFolderAtual);
			LOGGER.info("Personagem mudou de fase: " + faseFolderAnterior + " -> " + faseFolderAtual);
		}
	}
}
