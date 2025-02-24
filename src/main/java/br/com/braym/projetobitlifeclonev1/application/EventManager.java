package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import br.com.braym.projetobitlifeclonev1.utils.EventoJSONReader;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;
import br.com.braym.projetobitlifeclonev1.utils.RandomUtils;
import java.util.List;
import java.util.logging.Logger;

public class EventManager {
    private static final Logger LOGGER = Logger.getLogger(EventManager.class.getName());
    private List<Evento> eventos;
    private String baseFolder; // Ex: "Eventos"

    /**
     * Construtor que, com base na idade do personagem, monta o caminho do JSON.
     */
    public EventManager(Personagem personagem, String baseFolder) {
        this.baseFolder = baseFolder;
        carregarEventos(personagem);
    }

    /**
     * Carrega os eventos do arquivo JSON com base na fase da vida do personagem.
     */
    public void carregarEventos(Personagem personagem) {
        String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        // Monta o caminho relativo: por exemplo, "Eventos/07-Juventude_22-29/eventos.json"
        String filePath = baseFolder + "/" + faseFolder + "/eventos.json";
        eventos = EventoJSONReader.lerEventos(filePath);
        if (eventos == null || eventos.isEmpty()) {
            LOGGER.warning("Nenhum evento encontrado no arquivo JSON: " + filePath);
        }
    }

    /**
     * Seleciona aleatoriamente um evento e o executa com o personagem fornecido.
     * Antes de processar, atualiza (recarrega) os eventos com base na idade atual.
     * @param personagem o personagem a ser afetado
     */
    public void processarEvento(Personagem personagem) {
        carregarEventos(personagem); // Recarrega os eventos conforme a fase atual
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
