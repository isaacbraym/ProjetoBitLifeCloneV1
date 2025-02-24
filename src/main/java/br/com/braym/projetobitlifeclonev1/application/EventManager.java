package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import br.com.braym.projetobitlifeclonev1.utils.EventoJSONReader;
import br.com.braym.projetobitlifeclonev1.utils.RandomUtils;
import java.util.List;
import java.util.logging.Logger;

/**
 * Gerencia os eventos do jogo, centralizando a leitura, validação e execução.
 */
public class EventManager {
    private static final Logger LOGGER = Logger.getLogger(EventManager.class.getName());
    private List<Evento> eventos;

    public EventManager(String eventosJsonFile) {
        eventos = EventoJSONReader.lerEventos(eventosJsonFile);
        if (eventos == null || eventos.isEmpty()) {
            LOGGER.warning("Nenhum evento encontrado no arquivo JSON: " + eventosJsonFile);
        }
    }

    /**
     * Seleciona aleatoriamente um evento e o executa com o personagem fornecido.
     * @param personagem o personagem a ser afetado
     */
    public void processarEvento(Personagem personagem) {
        if (eventos == null || eventos.isEmpty()) {
            System.out.println("Nenhum evento disponível para processar.");
            return;
        }
        int indiceAleatorio = RandomUtils.gerarNumero(0, eventos.size() - 1);
        Evento eventoSelecionado = eventos.get(indiceAleatorio);
        System.out.println("Evento selecionado: " + eventoSelecionado.getDescricao());
        if (eventoSelecionado instanceof EventoInterface) {
            ((EventoInterface) eventoSelecionado).executarEvento(personagem);
        } else {
            System.out.println("Evento não implementa interface executável.");
        }
    }
}
