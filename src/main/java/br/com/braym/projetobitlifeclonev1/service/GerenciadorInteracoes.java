package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.domain.Relacionamento;
import br.com.braym.projetobitlifeclonev1.domain.TipoRelacionamento;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;
import br.com.braym.projetobitlifeclonev1.utils.LeitorJSON;
import br.com.braym.projetobitlifeclonev1.utils.UtilitarioAleatorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gerencia as interações disponíveis para personagens com base na idade
 * e carrega textos dinamicamente de arquivos JSON.
 */
public class GerenciadorInteracoes {
    private static final Logger LOGGER = Logger.getLogger(GerenciadorInteracoes.class.getName());
    
    private final String pastaBases;
    private final LeitorJSON leitorJSON;
    
    // Cache de interações disponíveis por fase da vida
    private final Map<String, List<String>> interacoesPorFase;
    
    // Cache de textos de interação por tipo
    private final Map<String, Map<String, List<InteracaoInfo>>> textosInteracao;
    
    // Registro de interações já usadas
    private final Map<String, List<String>> interacoesUsadas;
    
    /**
     * Construtor que inicializa o gerenciador com uma pasta base
     * @param pastaBases Pasta onde estão os arquivos de interações
     */
    public GerenciadorInteracoes(String pastaBases) {
        this.pastaBases = pastaBases;
        this.leitorJSON = new LeitorJSON();
        this.interacoesPorFase = new HashMap<>();
        this.textosInteracao = new HashMap<>();
        this.interacoesUsadas = new HashMap<>();
        
        inicializarInteracoesDisponiveis();
    }
    
    /**
     * Inicializa as interações disponíveis por fase da vida
     * Método temporário até implementação completa da leitura de JSON
     */
    private void inicializarInteracoesDisponiveis() {
        // Fase 1: Primeira Infância (0-3 anos) - nenhuma interação disponível
        interacoesPorFase.put("01-PrimeiraInfancia_0-3", new ArrayList<>());
        
        // Fase 2: Segunda Infância (3-6 anos)
        List<String> fase2 = new ArrayList<>();
        fase2.add("brincar");
        fase2.add("sorrir");
        interacoesPorFase.put("02-SegundaInfancia_3-6", fase2);
        
        // Fase 3: Terceira Infância (6-11 anos)
        List<String> fase3 = new ArrayList<>();
        fase3.add("brincar");
        fase3.add("conversar");
        fase3.add("sorrir");
        interacoesPorFase.put("03-TerceiraInfancia_6-10", fase3);
        
        // Fase 4: Adolescência Inicial (11-14 anos)
        List<String> fase4 = new ArrayList<>();
        fase4.add("conversar");
        fase4.add("presente");
        fase4.add("brincar");
        fase4.add("passear");
        interacoesPorFase.put("04-AdolescenciaInicial_11-14", fase4);
        
        // Demais fases - adolescentes e adultos
        List<String> faseAdolescente = new ArrayList<>();
        faseAdolescente.add("conversar");
        faseAdolescente.add("presente");
        faseAdolescente.add("insultar");
        faseAdolescente.add("passear");
        
        interacoesPorFase.put("05-AdolescenciaMedia_15-17", faseAdolescente);
        interacoesPorFase.put("06-AdolescenciaTardia_18-21", faseAdolescente);
        interacoesPorFase.put("07-Juventude_22-29", faseAdolescente);
        interacoesPorFase.put("08-AdultoJovem_30-39", faseAdolescente);
        interacoesPorFase.put("09-MeiaIdade_40-59", faseAdolescente);
        interacoesPorFase.put("10-IdosoJovem_60-74", faseAdolescente);
        interacoesPorFase.put("11-IdosoMaduro_75-89", faseAdolescente);
        interacoesPorFase.put("12-VelhiceAvancada_90", faseAdolescente);
        
        LOGGER.info("Interações disponíveis inicializadas para " + interacoesPorFase.size() + " fases");
    }
    
    /**
     * Obtém as interações disponíveis para um personagem com base na idade
     * @param personagem Personagem cujas interações serão determinadas
     * @return Lista de tipos de interação disponíveis
     */
    public List<String> obterInteracoesDisponiveis(Personagem personagem) {
        String fase = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        return interacoesPorFase.getOrDefault(fase, new ArrayList<>());
    }
    
    /**
     * Executa uma interação entre o personagem e um relacionamento
     * @param personagem Personagem que está interagindo
     * @param relacionamento Relacionamento com quem interagir
     * @param tipoInteracao Tipo de interação (conversar, presente, insultar, etc)
     * @return Texto descrevendo a interação e seus efeitos
     */
    public String executarInteracao(Personagem personagem, Relacionamento relacionamento, String tipoInteracao) {
        // Verifica disponibilidade da interação para a idade atual
        List<String> interacoesDisponiveis = obterInteracoesDisponiveis(personagem);
        if (!interacoesDisponiveis.contains(tipoInteracao)) {
            return "Esta interação não está disponível para sua idade atual.";
        }
        
        // Implementação temporária - será substituída pela leitura de JSON
        String mensagem;
        int modificadorNivel;
        int modificadorFelicidade;
        
        // Define valores de efeito baseados no tipo de interação
        switch (tipoInteracao.toLowerCase()) {
            case "conversar":
                modificadorNivel = UtilitarioAleatorio.gerarNumero(1, 8);
                modificadorFelicidade = UtilitarioAleatorio.gerarNumero(1, 4);
                mensagem = "Você teve uma conversa agradável com " + relacionamento.getPessoa().getNomeCompleto() + ".";
                break;
                
            case "presente":
                modificadorNivel = UtilitarioAleatorio.gerarNumero(5, 15);
                modificadorFelicidade = UtilitarioAleatorio.gerarNumero(3, 8);
                mensagem = "Você deu um presente para " + relacionamento.getPessoa().getNomeCompleto() + 
                           ". Eles ficaram muito felizes!";
                personagem.alterarFinancas(-UtilitarioAleatorio.gerarNumero(10, 50)); // Custo do presente
                break;
                
            case "insultar":
                modificadorNivel = -UtilitarioAleatorio.gerarNumero(10, 20);
                modificadorFelicidade = -UtilitarioAleatorio.gerarNumero(5, 10);
                mensagem = "Você insultou " + relacionamento.getPessoa().getNomeCompleto() + 
                           ". Eles ficaram chateados!";
                
                // Chance de retaliação
                if (UtilitarioAleatorio.eventoAcontece(30)) {
                    mensagem += " Eles revidaram o insulto.";
                    personagem.alterarFelicidade(-UtilitarioAleatorio.gerarNumero(3, 8));
                }
                
                // Chance de término em relacionamentos românticos
                if ((relacionamento.getTipo() == TipoRelacionamento.NAMORO || 
                     relacionamento.getTipo() == TipoRelacionamento.CASAMENTO) && 
                    UtilitarioAleatorio.eventoAcontece(10)) {
                    mensagem += " O relacionamento foi seriamente abalado e pode estar em risco.";
                }
                break;
                
            case "brincar":
                modificadorNivel = UtilitarioAleatorio.gerarNumero(3, 10);
                modificadorFelicidade = UtilitarioAleatorio.gerarNumero(3, 7);
                mensagem = "Você brincou com " + relacionamento.getPessoa().getNomeCompleto() + 
                           ". Foi muito divertido!";
                break;
                
            case "sorrir":
                modificadorNivel = UtilitarioAleatorio.gerarNumero(1, 3);
                modificadorFelicidade = UtilitarioAleatorio.gerarNumero(1, 2);
                mensagem = "Você sorriu para " + relacionamento.getPessoa().getNomeCompleto() + ".";
                break;
                
            case "passear":
                modificadorNivel = UtilitarioAleatorio.gerarNumero(4, 12);
                modificadorFelicidade = UtilitarioAleatorio.gerarNumero(3, 8);
                mensagem = "Você saiu para passear com " + relacionamento.getPessoa().getNomeCompleto() + 
                           ". Foi um tempo bem agradável.";
                personagem.alterarFinancas(-UtilitarioAleatorio.gerarNumero(5, 30)); // Custo do passeio
                break;
                
            default:
                return "Tipo de interação desconhecido: " + tipoInteracao;
        }
        
        // Aplica os efeitos no relacionamento e no personagem
        relacionamento.alterarNivel(modificadorNivel);
        personagem.alterarFelicidade(modificadorFelicidade);
        
        // Adiciona informação sobre o nível atual do relacionamento
        mensagem += " Nível de relacionamento: " + relacionamento.getNivel();
        
        return mensagem;
    }
    
    /**
     * Classe interna para representar dados das interações
     */
    public static class InteracaoInfo {
        private String texto;
        private int[] modificadorNivel;
        private int[] modificadorFelicidade;
        private int[] custo; // Para presentes
        private int chanceRetaliacao; // Para insultos
        private int chanceTermino; // Para insultos em relacionamentos românticos
        
        // Construtor vazio necessário para deserialização
        public InteracaoInfo() {
        }
        
        // Construtor completo
        public InteracaoInfo(String texto, int[] modificadorNivel, int[] modificadorFelicidade,
                            int[] custo, int chanceRetaliacao, int chanceTermino) {
            this.texto = texto;
            this.modificadorNivel = modificadorNivel;
            this.modificadorFelicidade = modificadorFelicidade;
            this.custo = custo;
            this.chanceRetaliacao = chanceRetaliacao;
            this.chanceTermino = chanceTermino;
        }
        
        // Getters
        public String getTexto() { return texto; }
        public int[] getModificadorNivel() { return modificadorNivel; }
        public int[] getModificadorFelicidade() { return modificadorFelicidade; }
        public int[] getCusto() { return custo; }
        public int getChanceRetaliacao() { return chanceRetaliacao; }
        public int getChanceTermino() { return chanceTermino; }
    }
}