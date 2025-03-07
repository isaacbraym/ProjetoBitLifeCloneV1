package br.com.braym.projetobitlifeclonev1;

import br.com.braym.projetobitlifeclonev1.application.FabricaJogo;
import br.com.braym.projetobitlifeclonev1.application.MotorJogo;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.infrastructure.persistence.GerenciadorSalvamentoJogo;
import br.com.braym.projetobitlifeclonev1.infrastructure.persistence.GerenciadorSalvamentoJogo.DadosJogoSalvo;
import br.com.braym.projetobitlifeclonev1.presentation.InterfaceConsole;
import br.com.braym.projetobitlifeclonev1.service.ProvedorEntrada;

import java.io.InputStream;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Classe principal da aplicação BitLife Clone. Ponto de entrada do jogo.
 */
public class AplicacaoPrincipal {
    private static final Logger LOGGER = Logger.getLogger(AplicacaoPrincipal.class.getName());
    private static final String PASTA_EVENTOS = "Eventos";

    /**
     * Método principal
     * 
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        try {
            configurarLog();
            LOGGER.info("Iniciando BitLife Clone v1.0");

            // Cria a fábrica e componentes básicos
            FabricaJogo fabrica = new FabricaJogo(PASTA_EVENTOS);
            ProvedorEntrada provedorEntrada = fabrica.criarProvedorEntrada();
            InterfaceConsole interfaceConsole = fabrica.criarInterfaceConsole(provedorEntrada);

            // Menu inicial
            exibirMenuInicial(interfaceConsole, provedorEntrada, fabrica);

            LOGGER.info("Encerrando aplicação normalmente");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro fatal na aplicação: " + e.getMessage(), e);
            System.err.println("Ocorreu um erro fatal: " + e.getMessage());
            System.err.println("Consulte os logs para mais detalhes.");
        }
    }

    /**
     * Configura o sistema de logs
     */
    private static void configurarLog() {
        try {
            // Tenta carregar o arquivo de configuração
            InputStream configStream = AplicacaoPrincipal.class.getResourceAsStream("/logging.properties");
            
            if (configStream != null) {
                LogManager.getLogManager().readConfiguration(configStream);
                LOGGER.info("Configuração de log carregada com sucesso.");
            } else {
                // Cria uma configuração padrão se o arquivo não for encontrado
                LOGGER.warning("Arquivo logging.properties não encontrado. Usando configuração padrão.");
                
                // Configura Logger raiz
                Logger rootLogger = Logger.getLogger("");
                rootLogger.setLevel(Level.INFO);
                
                // Limpa handlers existentes
                for (Handler handler : rootLogger.getHandlers()) {
                    rootLogger.removeHandler(handler);
                }
                
                // Adiciona handler de console
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.INFO);
                SimpleFormatter formatter = new SimpleFormatter();
                consoleHandler.setFormatter(formatter);
                rootLogger.addHandler(consoleHandler);
            }
        } catch (Exception e) {
            System.err.println("Aviso: Não foi possível configurar o log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exibe o menu inicial e processa a escolha do usuário
     * 
     * @param interfaceConsole Interface de console
     * @param provedorEntrada  Provedor de entrada
     * @param fabrica          Fábrica de componentes do jogo
     */
    private static void exibirMenuInicial(InterfaceConsole interfaceConsole, ProvedorEntrada provedorEntrada,
            FabricaJogo fabrica) {
        boolean sair = false;

        while (!sair) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("    BitLife Clone v1.0");
            System.out.println("=".repeat(30));
            System.out.println("1. Novo Jogo");
            System.out.println("2. Carregar Jogo");
            System.out.println("3. Sair");
            System.out.println("=".repeat(30));

            int escolha = provedorEntrada.lerInteiroComIntervalo("Escolha uma opção: ", 1, 3);

            switch (escolha) {
                case 1 -> iniciarNovoJogo(interfaceConsole, provedorEntrada, fabrica);
                case 2 -> carregarJogoSalvo(interfaceConsole, provedorEntrada, fabrica);
                case 3 -> sair = true;
            }
        }
    }

    /**
     * Inicia um novo jogo
     * 
     * @param interfaceConsole Interface de console
     * @param provedorEntrada  Provedor de entrada
     * @param fabrica          Fábrica de componentes do jogo
     */
    private static void iniciarNovoJogo(InterfaceConsole interfaceConsole, ProvedorEntrada provedorEntrada,
            FabricaJogo fabrica) {
        String nomePersonagem = provedorEntrada.lerLinhaObrigatoria("Digite o nome do seu personagem: ");

        Personagem personagem = fabrica.criarPersonagem(nomePersonagem);
        interfaceConsole.exibirBoasVindas(personagem);

        MotorJogo motor = fabrica.criarJogo(nomePersonagem);
        motor.executar();
    }

    /**
     * Carrega um jogo salvo
     * 
     * @param interfaceConsole Interface de console
     * @param provedorEntrada  Provedor de entrada
     * @param fabrica          Fábrica de componentes do jogo
     */
    private static void carregarJogoSalvo(InterfaceConsole interfaceConsole, ProvedorEntrada provedorEntrada,
            FabricaJogo fabrica) {
        GerenciadorSalvamentoJogo gerenciador = fabrica.criarGerenciadorSalvamento();
        List<DadosJogoSalvo> jogosSalvos = gerenciador.listarJogosSalvos();

        if (jogosSalvos.isEmpty()) {
            System.out.println("Não há jogos salvos disponíveis.");
            return;
        }

        System.out.println("\nDigite qual jogo você quer carregar:");
        for (int i = 0; i < jogosSalvos.size(); i++) {
            DadosJogoSalvo jogo = jogosSalvos.get(i);
            System.out.printf("%d - %s_%d_%s\n", i + 1, jogo.getNomePersonagem(), jogo.getIdade(),
                    jogo.getDataSalvamento());
        }

        int escolha = provedorEntrada.lerInteiroComIntervalo("Escolha: ", 1, jogosSalvos.size());
        String caminhoArquivo = jogosSalvos.get(escolha - 1).getCaminhoArquivo();

        Personagem personagemCarregado = gerenciador.carregarJogo(caminhoArquivo);

        if (personagemCarregado != null) {
            System.out.println("Jogo carregado com sucesso!");
            MotorJogo motor = fabrica.criarJogoComPersonagem(personagemCarregado);
            motor.executar();
        } else {
            System.out.println("Falha ao carregar o jogo. O arquivo pode estar corrompido.");
        }
    }
}