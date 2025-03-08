package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.domain.Pessoa;
import br.com.braym.projetobitlifeclonev1.domain.Profissao;
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
    private final CarregadorProfissoes carregadorProfissoes;
    private final GerenciadorInteracoes gerenciadorInteracoes;
    
    /**
     * Construtor que inicializa o gerenciador de relacionamentos
     */
    public GerenciadorRelacionamentos() {
        this.carregadorNomes = new CarregadorNomes();
        this.carregadorProfissoes = new CarregadorProfissoes();
        this.gerenciadorInteracoes = new GerenciadorInteracoes("Eventos");
    }
    
    /**
     * Construtor que inicializa o gerenciador com uma pasta base específica
     * @param pastaBases Pasta onde estão os arquivos de interações
     */
    public GerenciadorRelacionamentos(String pastaBases) {
        this.carregadorNomes = new CarregadorNomes();
        this.carregadorProfissoes = new CarregadorProfissoes();
        this.gerenciadorInteracoes = new GerenciadorInteracoes(pastaBases);
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
     * @param personagem Personagem que terá suas finanças definidas
     */
    public void criarFamiliaInicial(String sobrenomeFamilia, int idadePersonagem, Personagem personagem) {
        // Criar pais com idades entre 25 e 45 anos a mais que o personagem
        int idadePai = idadePersonagem + UtilitarioAleatorio.gerarNumero(25, 45);
        int idadeMae = idadePersonagem + UtilitarioAleatorio.gerarNumero(25, 40);
        
        // Selecionar profissões aleatórias
        Profissao profissaoPai = carregadorProfissoes.obterProfissaoMasculinaAleatoria();
        Profissao profissaoMae = carregadorProfissoes.obterProfissaoFemininaAleatoria();
        
        // Criar pai
        Pessoa pai = new Pessoa(
            carregadorNomes.obterNomeMasculinoAleatorio(),
            sobrenomeFamilia,
            idadePai,
            "Masculino",
            profissaoPai
        );
        adicionarRelacionamento(pai, TipoRelacionamento.PAI);
        LOGGER.info("Pai criado: " + pai.getNomeCompleto() + ", " + pai.getIdade() + " anos, " + 
                   profissaoPai.getNome() + " (R$" + pai.getSalario() + ",00)");
        
        // Criar mãe
        Pessoa mae = new Pessoa(
            carregadorNomes.obterNomeFemininoAleatorio(),
            sobrenomeFamilia,
            idadeMae,
            "Feminino",
            profissaoMae
        );
        adicionarRelacionamento(mae, TipoRelacionamento.MAE);
        LOGGER.info("Mãe criada: " + mae.getNomeCompleto() + ", " + mae.getIdade() + " anos, " + 
                   profissaoMae.getNome() + " (R$" + mae.getSalario() + ",00)");
        
        // Calcular e definir as finanças iniciais do personagem
        int financasIniciais = calcularFinancasIniciais(profissaoPai, profissaoMae);
        personagem.setFinancas(financasIniciais);
        LOGGER.info("Finanças iniciais do personagem definidas como R$" + financasIniciais + ",00");
    }
    
    /**
     * Calcular as finanças iniciais baseadas nas profissões dos pais
     * 
     * @param profissaoPai Profissão do pai
     * @param profissaoMae Profissão da mãe
     * @return Valor inicial das finanças
     */
    private int calcularFinancasIniciais(Profissao profissaoPai, Profissao profissaoMae) {
        int minimo = 0;
        int maximo = 0;
        
        // Se ambos os pais têm profissão
        if (profissaoPai != null && profissaoMae != null) {
            minimo = Math.max(profissaoPai.getFinancasMinimo(), profissaoMae.getFinancasMinimo());
            maximo = Math.max(profissaoPai.getFinancasMaximo(), profissaoMae.getFinancasMaximo());
        } 
        // Se só o pai tem profissão
        else if (profissaoPai != null) {
            minimo = profissaoPai.getFinancasMinimo();
            maximo = profissaoPai.getFinancasMaximo();
        } 
        // Se só a mãe tem profissão
        else if (profissaoMae != null) {
            minimo = profissaoMae.getFinancasMinimo();
            maximo = profissaoMae.getFinancasMaximo();
        } 
        // Se nenhum tem profissão, finanças baixas
        else {
            minimo = 0;
            maximo = 3000;
        }
        
        // Gera um valor aleatório dentro do intervalo
        return UtilitarioAleatorio.gerarNumero(minimo, maximo);
    }
    
    /**
     * Cria a família inicial para um personagem (método de compatibilidade)
     * @param sobrenomeFamilia Sobrenome da família
     * @param idadePersonagem Idade do personagem principal
     */
    public void criarFamiliaInicial(String sobrenomeFamilia, int idadePersonagem) {
        // Método mantido para compatibilidade com código existente
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
     * Obtém as interações disponíveis para um personagem com base na idade
     * @param personagem Personagem cujas interações serão determinadas
     * @return Lista de tipos de interação disponíveis
     */
    public List<String> obterInteracoesDisponiveis(Personagem personagem) {
        return gerenciadorInteracoes.obterInteracoesDisponiveis(personagem);
    }
    
    /**
     * Interage com um relacionamento específico usando as regras dinâmicas
     * @param personagem Personagem que está interagindo
     * @param idPessoa ID da pessoa com quem interagir
     * @param tipoInteracao Tipo de interação (conversar, presente, insultar, etc)
     * @return Mensagem descrevendo o resultado da interação
     */
    public String interagir(Personagem personagem, String idPessoa, String tipoInteracao) {
        Relacionamento relacionamento = relacionamentos.get(idPessoa);
        if (relacionamento == null) {
            return "Pessoa não encontrada.";
        }
        
        return gerenciadorInteracoes.executarInteracao(personagem, relacionamento, tipoInteracao);
    }

    /**
     * Método legado para manter compatibilidade com o código existente
     * @param idPessoa ID da pessoa com quem interagir
     * @param tipoInteracao Tipo de interação (conversar, presente, insultar)
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