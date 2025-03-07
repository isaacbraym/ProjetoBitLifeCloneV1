package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Pessoa;
import br.com.braym.projetobitlifeclonev1.domain.Relacionamento;
import br.com.braym.projetobitlifeclonev1.domain.TipoRelacionamento;
import br.com.braym.projetobitlifeclonev1.utils.UtilitarioAleatorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Gerencia os relacionamentos do personagem principal
 */
public class GerenciadorRelacionamentos {
    private static final Logger LOGGER = Logger.getLogger(GerenciadorRelacionamentos.class.getName());
    private final Map<String, Relacionamento> relacionamentos = new HashMap<>();
    private final CarregadorNomes carregadorNomes;
    
    /**
     * Construtor que inicializa o gerenciador de relacionamentos
     */
    public GerenciadorRelacionamentos() {
        this.carregadorNomes = new CarregadorNomes();
    }
    
    /**
     * Construtor que permite injetar um carregador de nomes
     * @param carregadorNomes Carregador de nomes a ser utilizado
     */
    public GerenciadorRelacionamentos(CarregadorNomes carregadorNomes) {
        this.carregadorNomes = carregadorNomes;
    }
    
    /**
     * Adiciona um novo relacionamento
     */
    public Relacionamento adicionarRelacionamento(Pessoa pessoa, TipoRelacionamento tipo) {
        Relacionamento relacionamento = new Relacionamento(pessoa, tipo);
        relacionamentos.put(pessoa.getId(), relacionamento);
        return relacionamento;
    }
    
    /**
     * Cria a família inicial para um personagem com determinada idade
     * @param sobrenomeFamilia Sobrenome da família
     * @param idadePersonagem Idade do personagem principal
     */
    public void criarFamiliaInicial(String sobrenomeFamilia, int idadePersonagem) {
        // Criar pais com idades entre 25 e 45 anos a mais que o personagem
        int idadePai = idadePersonagem + UtilitarioAleatorio.gerarNumero(25, 45);
        int idadeMae = idadePersonagem + UtilitarioAleatorio.gerarNumero(25, 40);
        
        // Criar pai
        Pessoa pai = new Pessoa(
            carregadorNomes.obterNomeMasculinoAleatorio(),
            sobrenomeFamilia,
            idadePai,
            "Masculino"
        );
        adicionarRelacionamento(pai, TipoRelacionamento.PAI);
        LOGGER.info("Pai criado: " + pai.getNomeCompleto() + ", " + pai.getIdade() + " anos");
        
        // Criar mãe
        Pessoa mae = new Pessoa(
            carregadorNomes.obterNomeFemininoAleatorio(),
            sobrenomeFamilia,
            idadeMae,
            "Feminino"
        );
        adicionarRelacionamento(mae, TipoRelacionamento.MAE);
        LOGGER.info("Mãe criada: " + mae.getNomeCompleto() + ", " + mae.getIdade() + " anos");
    }
    
    /**
     * Gera uma pessoa aleatória com idade próxima à fornecida
     */
    public Pessoa gerarPessoaAleatoria(int idadeAproximada) {
        boolean isMasculino = UtilitarioAleatorio.eventoAcontece(50);
        String nome;
        String genero;
        
        if (isMasculino) {
            nome = carregadorNomes.obterNomeMasculinoAleatorio();
            genero = "Masculino";
        } else {
            nome = carregadorNomes.obterNomeFemininoAleatorio();
            genero = "Feminino";
        }
        
        // Idade pode variar em até 5 anos para mais ou para menos
        int idade = idadeAproximada + UtilitarioAleatorio.gerarNumero(-5, 5);
        idade = Math.max(5, idade); // idade mínima de 5 anos
        
        String sobrenome = carregadorNomes.obterSobrenomeAleatorio();
        
        return new Pessoa(nome, sobrenome, idade, genero);
    }
    
    /**
     * Cria uma amizade aleatória
     */
    public Relacionamento criarAmizadeAleatoria(int idadePersonagem) {
        Pessoa pessoa = gerarPessoaAleatoria(idadePersonagem);
        return adicionarRelacionamento(pessoa, TipoRelacionamento.AMIZADE);
    }
    
    /**
     * Obtém todos os relacionamentos
     */
    public List<Relacionamento> getTodosRelacionamentos() {
        return new ArrayList<>(relacionamentos.values());
    }
    
    /**
     * Obtém relacionamentos por tipo
     */
    public List<Relacionamento> getRelacionamentosPorTipo(TipoRelacionamento tipo) {
        return relacionamentos.values().stream()
                .filter(r -> r.getTipo() == tipo)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtém um relacionamento específico por tipo (ex: pai, mãe)
     * Retorna o primeiro encontrado
     */
    public Relacionamento getRelacionamentoPorTipo(TipoRelacionamento tipo) {
        return relacionamentos.values().stream()
                .filter(r -> r.getTipo() == tipo)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtém o sobrenome da família a partir dos pais
     * @return Sobrenome da família ou um sobrenome aleatório se não houver pais
     */
    public String getSobrenomeFamilia() {
        Relacionamento relPai = getRelacionamentoPorTipo(TipoRelacionamento.PAI);
        if (relPai != null) {
            return relPai.getPessoa().getSobrenome();
        }
        
        Relacionamento relMae = getRelacionamentoPorTipo(TipoRelacionamento.MAE);
        if (relMae != null) {
            return relMae.getPessoa().getSobrenome();
        }
        
        return carregadorNomes.obterSobrenomeAleatorio();
    }
    
    /**
     * Avança o tempo para todos os relacionamentos
     */
    public void avancarTempo() {
        relacionamentos.values().forEach(Relacionamento::avancarTempo);
    }
    
    /**
     * Interage com um relacionamento específico
     * @return Mensagem descrevendo o resultado da interação
     */
    public String interagir(String idPessoa, String tipoInteracao) {
        Relacionamento relacionamento = relacionamentos.get(idPessoa);
        if (relacionamento == null) {
            return "Pessoa não encontrada.";
        }
        
        int modificadorNivel = 0;
        String mensagem = "";
        
        switch (tipoInteracao.toLowerCase()) {
            case "conversar":
                modificadorNivel = UtilitarioAleatorio.gerarNumero(1, 5);
                mensagem = "Você teve uma conversa com " + relacionamento.getPessoa().getNomeCompleto() + ".";
                break;
            case "presente":
                modificadorNivel = UtilitarioAleatorio.gerarNumero(5, 15);
                mensagem = "Você deu um presente para " + relacionamento.getPessoa().getNomeCompleto() + 
                           ". Eles ficaram muito felizes!";
                break;
            case "insultar":
                modificadorNivel = -UtilitarioAleatorio.gerarNumero(10, 20);
                mensagem = "Você insultou " + relacionamento.getPessoa().getNomeCompleto() + 
                           ". Eles ficaram chateados!";
                break;
            default:
                return "Tipo de interação desconhecido.";
        }
        
        relacionamento.alterarNivel(modificadorNivel);
        return mensagem + " Nível de relacionamento: " + relacionamento.getNivel();
    }
}