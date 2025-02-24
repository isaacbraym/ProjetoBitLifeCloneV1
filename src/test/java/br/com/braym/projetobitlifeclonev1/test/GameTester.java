package br.com.braym.projetobitlifeclonev1.test;

import br.com.braym.projetobitlifeclonev1.impl.ConsoleObservador;
import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.narrativa.Escolha;
import br.com.braym.projetobitlifeclonev1.narrativa.EventoNarrativa;
import br.com.braym.projetobitlifeclonev1.narrativa.GerenciadorEventosNarrativa;
import br.com.braym.projetobitlifeclonev1.utils.EventoJSONReader;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;
import br.com.braym.projetobitlifeclonev1.utils.RandomUtils;
import java.util.List;
import java.util.Scanner;

public class GameTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Personagem personagem = new Personagem("Braym");
        personagem.adicionarObservador(new ConsoleObservador());
        System.out.println("Personagem criado: " + personagem.getNome());
        System.out.println("Idade inicial: " + personagem.getIdade());
        
        boolean running = true;
        while (running) {
            exibirMenu();
            int opcao = lerOpcao(scanner);
            switch (opcao) {
                case 1:
                    mostrarStatus(personagem);
                    break;
                case 2:
                    personagem.envelhecer();
                    System.out.println("Idade após envelhecer: " + personagem.getIdade());
                    break;
                case 3:
                    processarEventoIntegrado(personagem, scanner);
                    break;
                case 4:
                    running = false;
                    System.out.println("Encerrando o jogo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }
    
    private static void exibirMenu() {
        System.out.println("\n==== BitLife Clone ====");
        System.out.println("1. Mostrar status do personagem");
        System.out.println("2. Envelhecer personagem");
        System.out.println("3. Processar evento aleatório");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    private static int lerOpcao(Scanner scanner) {
        int opcao = -1;
        try {
            opcao = Integer.parseInt(scanner.nextLine());
        } catch(NumberFormatException e) {
            // valor inválido
        }
        return opcao;
    }
    
    private static void mostrarStatus(Personagem personagem) {
        System.out.println("\n==== Status do Personagem ====");
        System.out.println("Nome: " + personagem.getNome());
        System.out.println("Idade: " + personagem.getIdade());
        System.out.println("Aparência: " + personagem.getAparencia());
        System.out.println("Saúde: " + personagem.getSaude());
        System.out.println("Sanidade: " + personagem.getSanidade());
        System.out.println("Felicidade: " + personagem.getFelicidade());
        System.out.println("Inteligência: " + personagem.getInteligencia());
        System.out.println("Carisma: " + personagem.getCarisma());
        System.out.println("Finanças: " + personagem.getFinancas());
    }
    
    /**
     * Processa um evento integrado:
     * - Carrega um evento aleatório do arquivo eventos.json da fase atual.
     * - Exibe suas opções e solicita a escolha do jogador.
     * - Em seguida, carrega o arquivo eventosNarrativa.json do mesmo diretório,
     *   busca o evento narrativo correspondente pelo id e executa a narrativa da opção escolhida.
     */
    private static void processarEventoIntegrado(Personagem personagem, Scanner scanner) {
        // Determina a fase do personagem
        String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        String eventosFilePath = "Eventos/" + faseFolder + "/eventos.json";
        List<Evento> eventos = EventoJSONReader.lerEventos(eventosFilePath);
        if (eventos == null || eventos.isEmpty()) {
            System.out.println("Nenhum evento disponível para processar.");
            return;
        }
        // Seleciona aleatoriamente um evento
        int indiceAleatorio = RandomUtils.gerarNumero(0, eventos.size() - 1);
        Evento eventoSelecionado = eventos.get(indiceAleatorio);
        System.out.println("Evento selecionado: " + eventoSelecionado.getDescricao());
        List<String> opcoes = eventoSelecionado.getOpcoes();
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i + 1) + ": " + opcoes.get(i));
        }
        System.out.print("Escolha uma opção: ");
        int escolha = scanner.nextInt() - 1;
        scanner.nextLine(); // consumir a quebra de linha
        if (escolha < 0 || escolha >= opcoes.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        
        // Agora, carrega o arquivo de eventos narrativos do mesmo diretório
        String narrativaFilePath = "Eventos/" + faseFolder + "/eventosNarrativa.json";
        GerenciadorEventosNarrativa gerenciadorNarrativa = new GerenciadorEventosNarrativa(personagem, "Eventos");
        // Busca o evento narrativo com o mesmo id do evento selecionado
        EventoNarrativa eventoNarrativo = gerenciadorNarrativa.buscarEventoPorId(eventoSelecionado.getId());
        if (eventoNarrativo != null) {
            if (escolha < eventoNarrativo.getOpcoes().size()) {
                Escolha escolhaNarrativa = eventoNarrativo.getOpcoes().get(escolha);
                System.out.println("\n" + escolhaNarrativa.getRetornoNarrativo());
                // Aplica os efeitos da escolha narrativa aos atributos do personagem
                gerenciadorNarrativa.aplicarEfeitos(personagem, escolhaNarrativa.getEfeitos());
            } else {
                System.out.println("Opção narrativa inválida.");
            }
        } else {
            System.out.println("Nenhum evento narrativo vinculado ao id " + eventoSelecionado.getId());
        }
    }
}
