package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.domain.Pessoa;
import br.com.braym.projetobitlifeclonev1.domain.Relacionamento;
import br.com.braym.projetobitlifeclonev1.domain.TipoRelacionamento;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import br.com.braym.projetobitlifeclonev1.impl.EventoImpl;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;
import br.com.braym.projetobitlifeclonev1.utils.LeitorJSON;
import br.com.braym.projetobitlifeclonev1.utils.UtilitarioAleatorio;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Gerencia os eventos do jogo, controlando o carregamento, processamento e
 * registro de eventos já utilizados para cada fase da vida.
 */
public class GerenciadorEventos {
    private static final Logger LOGGER = Logger.getLogger(GerenciadorEventos.class.getName());

    // Cache de eventos por fase
    private final Map<String, List<Evento>> eventosPorFase = new HashMap<>();

    // Registro de eventos já processados
    private final Map<String, Set<String>> eventosProcessadosPorFase = new HashMap<>();

    private final String pastaBases;
    private final ProvedorEntrada provedorEntrada;

    /**
     * Construtor com injeção de dependências
     * 
     * @param pastaBases      Pasta base onde estão os arquivos de eventos
     * @param provedorEntrada Serviço para ler entrada do usuário
     */
    public GerenciadorEventos(String pastaBases, ProvedorEntrada provedorEntrada) {
        this.pastaBases = pastaBases;
        this.provedorEntrada = provedorEntrada;
    }

    /**
     * Carrega os eventos para uma fase específica, se ainda não estiverem
     * carregados
     * 
     * @param faseFolder Pasta da fase
     */
    private void carregarEventosSeNecessario(String faseFolder) {
        if (!eventosPorFase.containsKey(faseFolder)) {
            String filePath = pastaBases + "/" + faseFolder + "/eventos.json";
            List<Evento> listaEventos = new LeitorJSON().lerEventos(filePath);
            eventosPorFase.put(faseFolder, listaEventos != null ? listaEventos : new ArrayList<>());
            eventosProcessadosPorFase.put(faseFolder, new HashSet<>());
            LOGGER.info("Carregados " + (listaEventos != null ? listaEventos.size() : 0) + " eventos para a fase "
                    + faseFolder);
        }
    }

    /**
     * Reinicia o registro de eventos processados para uma fase específica
     * 
     * @param faseFolder Pasta da fase a ser reiniciada
     */
    public void reiniciarFase(String faseFolder) {
        eventosProcessadosPorFase.put(faseFolder, new HashSet<>());
        LOGGER.info("Fase " + faseFolder + " reiniciada.");
    }

    /**
     * Processa um evento aleatório para o personagem
     * 
     * @param personagem Personagem que será afetado pelo evento
     * @return true se o evento foi processado com sucesso, false se não havia eventos disponíveis
     */
    public boolean processarEvento(Personagem personagem) {
        // Aqui modificamos para criar eventos de relacionamento narrativos em vez de interações diretas
        List<Relacionamento> relacionamentos = personagem.getGerenciadorRelacionamentos().getTodosRelacionamentos();
        if (!relacionamentos.isEmpty() && UtilitarioAleatorio.eventoAcontece(30)) {
            return processarEventoNarrativoRelacionamento(personagem, relacionamentos);
        }
        
        // Se não processou evento de relacionamento, processa evento normal
        String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        carregarEventosSeNecessario(faseFolder);

        List<Evento> todosEventosDaFase = eventosPorFase.get(faseFolder);
        Set<String> eventosUsados = eventosProcessadosPorFase.get(faseFolder);
        List<Evento> eventosDisponiveis = todosEventosDaFase.stream()
            .filter(e -> !eventosUsados.contains(e.getId()))
            .collect(Collectors.toList());

        if (eventosDisponiveis.isEmpty()) {
            System.out.println("Não há mais eventos disponíveis para esta fase.");
            return false;
        }

        // Usa o UtilitarioAleatorio unificado
        int indiceAleatorio = UtilitarioAleatorio.gerarNumero(0, eventosDisponiveis.size() - 1);
        Evento eventoSelecionado = eventosDisponiveis.get(indiceAleatorio);
        System.out.println("Evento selecionado: " + eventoSelecionado.getDescricao());

        // Executa o evento
        if (eventoSelecionado instanceof EventoInterface) {
            ((EventoInterface) eventoSelecionado).executarEvento(personagem);
        } else {
            new EventoImpl(
                eventoSelecionado.getId(), 
                eventoSelecionado.getDescricao(), 
                eventoSelecionado.getOpcoes(),
                eventoSelecionado.getEfeitos(), 
                eventoSelecionado.getAtributo(),
                eventoSelecionado.getEfeitosMultiplos(), 
                provedorEntrada.obterScanner()
            ).executarEvento(personagem);
        }

        eventosUsados.add(eventoSelecionado.getId());
        LOGGER.info("Evento " + eventoSelecionado.getId() + " marcado como usado.");
        return true;
    }
    
    /**
     * Processa um evento narrativo sobre relacionamento, sem interação direta
     * 
     * @param personagem Personagem afetado pelo evento
     * @param relacionamentos Lista de relacionamentos disponíveis
     * @return true se o evento foi processado com sucesso
     */
    private boolean processarEventoNarrativoRelacionamento(Personagem personagem, List<Relacionamento> relacionamentos) {
        // Seleciona um relacionamento aleatório
        Relacionamento rel = relacionamentos.get(UtilitarioAleatorio.gerarNumero(0, relacionamentos.size() - 1));
        Pessoa pessoa = rel.getPessoa();

        // Cria um evento narrativo sobre o relacionamento em vez de interação direta
        List<String> opcoes = new ArrayList<>();
        Map<String, Integer> efeitos = new HashMap<>();
        String descricao;
        
        // Escolhe um tipo de evento narrativo aleatório baseado no tipo de relacionamento
        int tipoEvento = UtilitarioAleatorio.gerarNumero(1, 3);
        
        switch (tipoEvento) {
            case 1: // Evento positivo
                descricao = "Você encontrou " + pessoa.getNomeCompleto() + " por acaso e tiveram um momento agradável juntos.";
                opcoes.add("Aproveitar o momento");
                opcoes.add("Manter distância");
                efeitos.put("felicidade", 5);
                break;
                
            case 2: // Evento neutro
                descricao = pessoa.getNomeCompleto() + " entrou em contato com você sobre assuntos pessoais.";
                opcoes.add("Ouvir com atenção");
                opcoes.add("Ser breve na conversa");
                efeitos.put("carisma", 3);
                break;
                
            case 3: // Evento desafiador
                descricao = "Você percebeu que " + pessoa.getNomeCompleto() + " está passando por um momento difícil.";
                opcoes.add("Oferecer ajuda");
                opcoes.add("Esperar que eles peçam ajuda");
                efeitos.put("sanidade", -2);
                efeitos.put("carisma", 4);
                break;
                
            default:
                descricao = "Você recebeu notícias de " + pessoa.getNomeCompleto() + ".";
                opcoes.add("Responder positivamente");
                opcoes.add("Ignorar");
                efeitos.put("felicidade", 2);
        }
        
        // Cria e executa um evento temporário
        Evento eventoTemporario = new Evento();
        eventoTemporario.setId("rel_temp_" + System.currentTimeMillis());
        eventoTemporario.setDescricao(descricao);
        eventoTemporario.setOpcoes(opcoes);
        eventoTemporario.setEfeitosMultiplos(efeitos);
        
        // Executa o evento
        new EventoImpl(eventoTemporario, provedorEntrada.obterScanner()).executarEvento(personagem);
        
        // Impacto no relacionamento
        rel.alterarNivel(UtilitarioAleatorio.gerarNumero(1, 5));
        
        return true;
    }

    /**
     * Retorna a quantidade de eventos disponíveis para a fase atual
     * 
     * @param personagem Personagem cuja fase será analisada
     * @return Número de eventos disponíveis
     */
    public int getQuantidadeEventosDisponiveis(Personagem personagem) {
        String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        carregarEventosSeNecessario(faseFolder);

        List<Evento> todosEventosDaFase = eventosPorFase.get(faseFolder);
        Set<String> eventosUsados = eventosProcessadosPorFase.get(faseFolder);

        return (int) todosEventosDaFase.stream().filter(e -> !eventosUsados.contains(e.getId())).count();
    }

    /**
     * Verifica se houve mudança de fase e atualiza conforme necessário
     * 
     * @param personagem    Personagem atual
     * @param idadeAnterior Idade anterior do personagem
     */
    public void verificarMudancaFase(Personagem personagem, int idadeAnterior) {
        String faseFolderAnterior = FaseDaVidaResolver.getFaseDaVidaFolder(idadeAnterior);
        String faseFolderAtual = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());

        if (!faseFolderAnterior.equals(faseFolderAtual)) {
            carregarEventosSeNecessario(faseFolderAtual);
            LOGGER.info("Personagem mudou de fase: " + faseFolderAnterior + " -> " + faseFolderAtual);
        }
    }
}