package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.utils.EventoJSONReader;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class EventoLoader {

	private final String baseFolder;
	private final Map<String, List<Evento>> eventosPorFase = new HashMap<>();
	private final Map<String, Set<String>> eventosProcessadosPorFase = new HashMap<>();

	public EventoLoader(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	public void carregarEventosSeNecessario(String faseFolder) {
		if (!eventosPorFase.containsKey(faseFolder)) {
			String filePath = baseFolder + "/" + faseFolder + "/eventos.json";
			List<Evento> listaEventos = EventoJSONReader.lerEventos(filePath);
			eventosPorFase.put(faseFolder, listaEventos != null ? listaEventos : new ArrayList<>());
			eventosProcessadosPorFase.put(faseFolder, new HashSet<>());
		}
	}

	public List<Evento> obterEventosPorFase(String faseFolder) {
		return eventosPorFase.getOrDefault(faseFolder, new ArrayList<>());
	}

	public Set<String> obterEventosProcessadosPorFase(String faseFolder) {
		return eventosProcessadosPorFase.getOrDefault(faseFolder, new HashSet<>());
	}

	public void reiniciarFase(String faseFolder) {
		eventosProcessadosPorFase.put(faseFolder, new HashSet<>());
	}
}
