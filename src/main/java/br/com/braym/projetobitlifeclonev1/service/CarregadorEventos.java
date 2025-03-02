package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.utils.EventoJSONReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Serviço responsável pelo carregamento de eventos do jogo.
 * Implementa o padrão Singleton para garantir uma única instância.
 */
public class CarregadorEventos {
    private static final Logger LOGGER = Logger.getLogger(CarregadorEventos.class.getName());
    
    private final String pastaBases;
    private final Map<String, List<Evento>> eventosPorFase;
    
    /**
     * Construtor que inicializa o carregador com a pasta base de eventos
     * @param pastaBases Caminho da pasta base onde estão os eventos
     */
    public CarregadorEventos(String pastaBases) {
        this.pastaBases = pastaBases;
        this.eventosPorFase = new ConcurrentHashMap<>();
    }
    
    /**
     * Carrega os eventos para uma determinada fase da vida
     * @param pastaDaFase Nome da pasta da fase
     * @return Lista de eventos disponíveis para a fase
     */
    public List<Evento> carregarEventosParaFase(String pastaDaFase) {
        if (!eventosPorFase.containsKey(pastaDaFase)) {
            String caminhoArquivo = pastaBases + "/" + pastaDaFase + "/eventos.json";
            List<Evento> listaEventos = EventoJSONReader.lerEventos(caminhoArquivo);
            eventosPorFase.put(pastaDaFase, listaEventos != null ? listaEventos : new ArrayList<>());
            LOGGER.info("Carregados " + (listaEventos != null ? listaEventos.size() : 0) + " evento(s) para a fase " + pastaDaFase);
        }
        return eventosPorFase.get(pastaDaFase);
    }
    
    /**
     * Verifica se já foram carregados eventos para uma determinada fase
     * @param pastaDaFase Nome da pasta da fase
     * @return true se já existem eventos carregados, false caso contrário
     */
    public boolean possuiEventosParaFase(String pastaDaFase) {
        return eventosPorFase.containsKey(pastaDaFase);
    }
    
    /**
     * Limpa o cache de eventos para uma fase específica
     * @param pastaDaFase Nome da pasta da fase a ser limpa
     */
    public void limparCacheDaFase(String pastaDaFase) {
        eventosPorFase.remove(pastaDaFase);
        LOGGER.info("Cache de eventos para fase " + pastaDaFase + " foi limpo.");
    }
}