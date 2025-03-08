package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Profissao;
import br.com.braym.projetobitlifeclonev1.utils.UtilitarioAleatorio;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Responsável por carregar e disponibilizar profissões para personagens do jogo.
 */
public class CarregadorProfissoes {
    private static final Logger LOGGER = Logger.getLogger(CarregadorProfissoes.class.getName());
    
    private final List<Profissao> profissoesMasculinas = new ArrayList<>();
    private final List<Profissao> profissoesFemininas = new ArrayList<>();
    
    // CORREÇÃO DOS CAMINHOS: agora apontam para a pasta Financas
    private static final String CAMINHO_PROFISSOES_MASCULINAS = "/Financas/profissoes_masculinas.json";
    private static final String CAMINHO_PROFISSOES_FEMININAS = "/Financas/profissoes_femininas.json";
    
    /**
     * Construtor que carrega todas as profissões disponíveis.
     */
    public CarregadorProfissoes() {
    	verificarRecursosDisponiveis();
        carregarProfissoesMasculinas();
        carregarProfissoesFemininas();
    }
    public void verificarRecursosDisponiveis() {
        LOGGER.info("Verificando disponibilidade de recursos...");
        
        // Tenta carregar cada um dos arquivos para verificar se estão acessíveis
        String[] arquivos = {
            CAMINHO_PROFISSOES_MASCULINAS,
            CAMINHO_PROFISSOES_FEMININAS,
            "/Interacoes/interacoes_disponiveis.json", 
            "/Interacoes/conversar.json",
            "/Interacoes/presente.json",
            "/Interacoes/insultar.json"
        };
        
        for (String arquivo : arquivos) {
            try (InputStream is = getClass().getResourceAsStream(arquivo)) {
                if (is != null) {
                    LOGGER.info("Arquivo encontrado: " + arquivo);
                } else {
                    LOGGER.warning("Arquivo NÃO encontrado: " + arquivo);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Erro ao verificar arquivo: " + arquivo, e);
            }
        }
    }
    /**
     * Carrega as profissões masculinas do arquivo de recursos.
     */
    private void carregarProfissoesMasculinas() {
        try {
            String conteudoJson = lerArquivoJson(CAMINHO_PROFISSOES_MASCULINAS);
            List<Profissao> profissoes = extrairProfissoes(conteudoJson);
            profissoesMasculinas.addAll(profissoes);
            LOGGER.info("Carregadas " + profissoesMasculinas.size() + " profissões masculinas");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao carregar profissões masculinas", e);
            // Adiciona algumas profissões padrão caso falhe
            profissoesMasculinas.add(new Profissao("Professor", 3000, 5500, 2000, 15000));
            profissoesMasculinas.add(new Profissao("Médico", 8300, 12500, 12000, 75000));
            profissoesMasculinas.add(new Profissao("Engenheiro", 5000, 10000, 8000, 50000));
            profissoesMasculinas.add(new Profissao("Advogado", 6000, 15000, 10000, 60000));
            // Apenas 20% de chance de desempregado
            if (UtilitarioAleatorio.eventoAcontece(20)) {
                profissoesMasculinas.add(new Profissao("Desempregado", 0, 1000, 0, 3000));
            }
        }
    }
    
    /**
     * Carrega as profissões femininas do arquivo de recursos.
     */
    private void carregarProfissoesFemininas() {
        try {
            String conteudoJson = lerArquivoJson(CAMINHO_PROFISSOES_FEMININAS);
            List<Profissao> profissoes = extrairProfissoes(conteudoJson);
            profissoesFemininas.addAll(profissoes);
            LOGGER.info("Carregadas " + profissoesFemininas.size() + " profissões femininas");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao carregar profissões femininas", e);
            // Adiciona algumas profissões padrão caso falhe
            profissoesFemininas.add(new Profissao("Professora", 3000, 5500, 2000, 15000));
            profissoesFemininas.add(new Profissao("Médica", 8300, 12500, 12000, 75000));
            profissoesFemininas.add(new Profissao("Engenheira", 5000, 10000, 8000, 50000));
            profissoesFemininas.add(new Profissao("Advogada", 6000, 15000, 10000, 60000));
            // Apenas 20% de chance de desempregada
            if (UtilitarioAleatorio.eventoAcontece(20)) {
                profissoesFemininas.add(new Profissao("Desempregada", 0, 1000, 0, 3000));
            }
        }
    }
    
    /**
     * Lê o conteúdo de um arquivo JSON.
     * 
     * @param caminhoArquivo Caminho do arquivo de recursos
     * @return Conteúdo do arquivo como string
     * @throws Exception Em caso de erro de leitura
     */
    private String lerArquivoJson(String caminhoArquivo) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(caminhoArquivo)) {
            if (is == null) {
                LOGGER.severe("Arquivo não encontrado no caminho: " + caminhoArquivo);
                // Tentar caminho alternativo
                String caminhoAlternativo = caminhoArquivo.replace("/Financas/", "/Profissoes/");
                LOGGER.info("Tentando caminho alternativo: " + caminhoAlternativo);
                InputStream isAlt = getClass().getResourceAsStream(caminhoAlternativo);
                if (isAlt == null) {
                    throw new Exception("Arquivo não encontrado: " + caminhoArquivo);
                }
                byte[] bytes = isAlt.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
    
    /**
     * Obtém uma profissão masculina aleatória.
     * 
     * @return Profissão masculina aleatória
     */
    public Profissao obterProfissaoMasculinaAleatoria() {
        if (profissoesMasculinas.isEmpty()) {
            // Se a lista estiver vazia após tentar carregar, cria uma profissão padrão na hora
            int tipoRandom = UtilitarioAleatorio.gerarNumero(1, 5);
            switch (tipoRandom) {
                case 1: return new Profissao("Professor", 3000, 5500, 2000, 15000);
                case 2: return new Profissao("Engenheiro", 5000, 10000, 8000, 50000);
                case 3: return new Profissao("Médico", 8300, 12500, 12000, 75000);
                case 4: return new Profissao("Advogado", 6000, 15000, 10000, 60000);
                default: return new Profissao("Desempregado", 0, 1000, 0, 3000);
            }
        }
        return profissoesMasculinas.get(UtilitarioAleatorio.gerarNumero(0, profissoesMasculinas.size() - 1));
    }
    
    /**
     * Obtém uma profissão feminina aleatória.
     * 
     * @return Profissão feminina aleatória
     */
    public Profissao obterProfissaoFemininaAleatoria() {
        if (profissoesFemininas.isEmpty()) {
            // Se a lista estiver vazia após tentar carregar, cria uma profissão padrão na hora
            int tipoRandom = UtilitarioAleatorio.gerarNumero(1, 5);
            switch (tipoRandom) {
                case 1: return new Profissao("Professora", 3000, 5500, 2000, 15000);
                case 2: return new Profissao("Engenheira", 5000, 10000, 8000, 50000);
                case 3: return new Profissao("Médica", 8300, 12500, 12000, 75000);
                case 4: return new Profissao("Advogada", 6000, 15000, 10000, 60000);
                default: return new Profissao("Desempregada", 0, 1000, 0, 3000);
            }
        }
        return profissoesFemininas.get(UtilitarioAleatorio.gerarNumero(0, profissoesFemininas.size() - 1));
    }
    
    // Outros métodos continuam os mesmos...
    private List<Profissao> extrairProfissoes(String conteudoJson) {
        List<Profissao> profissoes = new ArrayList<>();
        Gson gson = new Gson();
        
        try {
            JsonObject objetoJson = gson.fromJson(conteudoJson, JsonObject.class);
            JsonArray arrayProfissoes = objetoJson.getAsJsonArray("profissoes");
            
            for (JsonElement elemento : arrayProfissoes) {
                JsonObject objetoProfissao = elemento.getAsJsonObject();
                Profissao profissao = new Profissao(
                    objetoProfissao.get("nome").getAsString(),
                    objetoProfissao.get("salarioMinimo").getAsInt(),
                    objetoProfissao.get("salarioMaximo").getAsInt(),
                    objetoProfissao.get("financasMinimo").getAsInt(),
                    objetoProfissao.get("financasMaximo").getAsInt()
                );
                profissoes.add(profissao);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao extrair profissões do JSON", e);
        }
        
        return profissoes;
    }
}