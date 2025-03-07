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
        // Chance de eventos de relacionamento (se tiver relacionamentos)
        List<Relacionamento> relacionamentos = personagem.getGerenciadorRelacionamentos().getTodosRelacionamentos();
        if (!relacionamentos.isEmpty() && UtilitarioAleatorio.eventoAcontece(30)) {
            return processarEventoRelacionamento(personagem, relacionamentos);
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
     * Processa um evento específico de relacionamento
     * 
     * @param personagem Personagem afetado pelo evento
     * @param relacionamentos Lista de relacionamentos disponíveis
     * @return true se o evento foi processado com sucesso
     */
    private boolean processarEventoRelacionamento(Personagem personagem, List<Relacionamento> relacionamentos) {
        // Seleciona um relacionamento aleatório
        Relacionamento rel = relacionamentos.get(UtilitarioAleatorio.gerarNumero(0, relacionamentos.size() - 1));
        Pessoa pessoa = rel.getPessoa();

        System.out.println("Evento de relacionamento com " + pessoa.getNome());
        System.out.println("Vocês são " + rel.getTipo().getDescricao() + " há " + rel.getTempo() + " anos.");

        // Opções de interação baseadas no tipo de relacionamento
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Passar um tempo juntos");
        opcoes.add("Dar um presente");

        if (rel.getTipo() == TipoRelacionamento.AMIZADE && rel.getNivel() > 70) {
            opcoes.add("Propor namoro");
        }

        if (rel.getTipo() == TipoRelacionamento.NAMORO && rel.getNivel() > 80 && rel.getTempo() > 2) {
            opcoes.add("Propor casamento");
        }

        // Mostrar opções
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i + 1) + ". " + opcoes.get(i));
        }

        // Processar escolha
        int escolha = provedorEntrada.lerInteiroComIntervalo("Escolha: ", 1, opcoes.size()) - 1;
        String opcaoEscolhida = opcoes.get(escolha);

        // Processar efeito da escolha
        switch (opcaoEscolhida) {
            case "Passar um tempo juntos":
                int ganho = UtilitarioAleatorio.gerarNumero(5, 15);
                rel.alterarNivel(ganho);
                System.out.println("Vocês passaram um bom tempo juntos. O nível de relacionamento aumentou para " + rel.getNivel());
                personagem.alterarFelicidade(5);
                break;

            case "Dar um presente":
                int ganhoPresente = UtilitarioAleatorio.gerarNumero(10, 25);
                rel.alterarNivel(ganhoPresente);
                System.out.println(pessoa.getNome() + " adorou seu presente! O nível de relacionamento aumentou para " + rel.getNivel());
                personagem.alterarFelicidade(8);
                personagem.alterarFinancas(-50); // Custo do presente
                break;

            case "Propor namoro":
                if (UtilitarioAleatorio.eventoAcontece(rel.getNivel())) {
                    rel.setTipo(TipoRelacionamento.NAMORO);
                    System.out.println(pessoa.getNome() + " aceitou seu pedido de namoro!");
                    personagem.alterarFelicidade(20);
                } else {
                    rel.alterarNivel(-20);
                    System.out.println(pessoa.getNome() + " recusou seu pedido. O relacionamento esfriou.");
                    personagem.alterarFelicidade(-15);
                }
                break;

            case "Propor casamento":
                if (UtilitarioAleatorio.eventoAcontece(rel.getNivel() - 10)) {
                    rel.setTipo(TipoRelacionamento.CASAMENTO);
                    System.out.println(pessoa.getNome() + " aceitou seu pedido de casamento! Vocês agora são casados.");
                    personagem.alterarFelicidade(30);
                } else {
                    rel.alterarNivel(-30);
                    System.out.println(pessoa.getNome() + " recusou seu pedido de casamento. O relacionamento sofreu bastante.");
                    personagem.alterarFelicidade(-25);
                }
                break;
        }

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