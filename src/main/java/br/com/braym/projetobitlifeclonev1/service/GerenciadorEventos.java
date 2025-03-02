package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import br.com.braym.projetobitlifeclonev1.impl.EventoImpl;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;
import br.com.braym.projetobitlifeclonev1.utils.UtilitarioAleatorio;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Gerencia os eventos do jogo, controlando o processamento e registro
 * de eventos já utilizados para cada fase da vida.
 */
public class GerenciadorEventos {
    private static final Logger LOGGER = Logger.getLogger(GerenciadorEventos.class.getName());

    private final CarregadorEventos carregadorEventos;
    private final Map<String, Set<String>> eventosProcessadosPorFase;
    private final ProvedorEntrada provedorEntrada;

    /**
     * Construtor com injeção de dependências
     * @param carregadorEventos Serviço para carregar eventos
     * @param provedorEntrada Serviço para ler entrada do usuário
     */
    public GerenciadorEventos(CarregadorEventos carregadorEventos, ProvedorEntrada provedorEntrada) {
        this.carregadorEventos = carregadorEventos;
        this.provedorEntrada = provedorEntrada;
        this.eventosProcessadosPorFase = new HashMap<>();
    }

    /**
     * Reinicia o registro de eventos processados para uma fase específica
     * @param pastaDaFase Nome da pasta da fase a ser reiniciada
     */
    public void reiniciarFase(String pastaDaFase) {
        eventosProcessadosPorFase.put(pastaDaFase, new HashSet<>());
        LOGGER.info("Fase " + pastaDaFase + " reiniciada.");
    }

    /**
     * Processa um evento aleatório para o personagem
     * @param personagem Personagem que será afetado pelo evento
     * @return true se o evento foi processado com sucesso, false se não havia eventos disponíveis
     */
    public boolean processarEvento(Personagem personagem) {
        // Usando o método getFaseDaVidaFolder para compatibilidade
        String pastaDaFase = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        
        // Carrega eventos se necessário
        List<Evento> todosEventosDaFase = carregadorEventos.carregarEventosParaFase(pastaDaFase);
        
        // Garante que o conjunto de eventos processados existe
        eventosProcessadosPorFase.putIfAbsent(pastaDaFase, new HashSet<>());
        Set<String> eventosUsados = eventosProcessadosPorFase.get(pastaDaFase);
        
        // Filtra eventos disponíveis
        List<Evento> eventosDisponiveis = todosEventosDaFase.stream()
                .filter(e -> !eventosUsados.contains(e.getId()))
                .collect(Collectors.toList());

        if (eventosDisponiveis.isEmpty()) {
            System.out.println("Não há mais eventos disponíveis para esta fase.");
            return false;
        }

        // Seleciona evento aleatório
        Evento eventoSelecionado = eventosDisponiveis.get(UtilitarioAleatorio.gerarNumero(0, eventosDisponiveis.size() - 1));
        System.out.println("Evento selecionado: " + eventoSelecionado.getDescricao());

        // Executa o evento
        executarEvento(eventoSelecionado, personagem);

        // Marca evento como usado
        eventosUsados.add(eventoSelecionado.getId());
        LOGGER.info("Evento " + eventoSelecionado.getId() + " marcado como usado.");
        return true;
    }

    /**
     * Executa um evento específico para o personagem
     * @param evento Evento a ser executado
     * @param personagem Personagem que será afetado
     */
    private void executarEvento(Evento evento, Personagem personagem) {
        if (evento instanceof EventoInterface) {
            ((EventoInterface) evento).executarEvento(personagem);
        } else {
            new EventoImpl(evento.getId(), 
                          evento.getDescricao(), 
                          evento.getOpcoes(),
                          evento.getEfeitos(), 
                          evento.getAtributo(), 
                          evento.getEfeitosMultiplos(), 
                          provedorEntrada.obterScanner())
                    .executarEvento(personagem);
        }
    }

    /**
     * Retorna a quantidade de eventos ainda não processados para a fase atual do personagem
     * @param personagem Personagem cuja fase será verificada
     * @return Número de eventos disponíveis
     */
    public int getQuantidadeEventosDisponiveis(Personagem personagem) {
        // Usando o método getFaseDaVidaFolder para compatibilidade
        String pastaDaFase = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        List<Evento> todosEventosDaFase = carregadorEventos.carregarEventosParaFase(pastaDaFase);
        
        eventosProcessadosPorFase.putIfAbsent(pastaDaFase, new HashSet<>());
        Set<String> eventosUsados = eventosProcessadosPorFase.get(pastaDaFase);
        
        return (int) todosEventosDaFase.stream()
                .filter(e -> !eventosUsados.contains(e.getId()))
                .count();
    }

    /**
     * Verifica se houve mudança de fase da vida e atualiza o carregamento de eventos
     * @param personagem Personagem que pode ter mudado de fase
     * @param idadeAnterior Idade anterior do personagem
     */
    public void verificarMudancaFase(Personagem personagem, int idadeAnterior) {
        // Usando o método getFaseDaVidaFolder para compatibilidade
        String pastaDaFaseAnterior = FaseDaVidaResolver.getFaseDaVidaFolder(idadeAnterior);
        String pastaDaFaseAtual = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());

        if (!pastaDaFaseAnterior.equals(pastaDaFaseAtual)) {
            carregadorEventos.carregarEventosParaFase(pastaDaFaseAtual);
            LOGGER.info("Personagem mudou de fase: " + pastaDaFaseAnterior + " -> " + pastaDaFaseAtual);
        }
    }
}