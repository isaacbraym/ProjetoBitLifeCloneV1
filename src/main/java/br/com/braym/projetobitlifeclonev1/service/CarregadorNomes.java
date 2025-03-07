package br.com.braym.projetobitlifeclonev1.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Responsável por carregar e disponibilizar nomes para personagens do jogo.
 */
public class CarregadorNomes {
    private static final Logger LOGGER = Logger.getLogger(CarregadorNomes.class.getName());
    
    private final List<String> nomesMasculinos = new ArrayList<>();
    private final List<String> nomesFemininos = new ArrayList<>();
    private final List<String> sobrenomes = new ArrayList<>();
    private final Random random = new Random();
    
    private static final String CAMINHO_NOMES_MASCULINOS = "/Nomes/NomesMasculinos.json";
    private static final String CAMINHO_NOMES_FEMININOS = "/Nomes/NomesFemininos.json";
    private static final String CAMINHO_SOBRENOMES = "/Nomes/Sobrenomes.json";
    
    /**
     * Construtor que carrega todos os nomes disponíveis.
     */
    public CarregadorNomes() {
        carregarNomesMasculinos();
        carregarNomesFemininos();
        carregarSobrenomes();
    }
    
    /**
     * Carrega os nomes masculinos do arquivo de recursos.
     */
    private void carregarNomesMasculinos() {
        try {
            nomesMasculinos.addAll(carregarLista(CAMINHO_NOMES_MASCULINOS));
            LOGGER.info("Carregados " + nomesMasculinos.size() + " nomes masculinos");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Erro ao carregar nomes masculinos", e);
            // Adiciona alguns nomes padrão caso falhe
            nomesMasculinos.addAll(Arrays.asList("João", "Pedro", "Carlos", "Miguel", "Lucas"));
        }
    }
    
    /**
     * Carrega os nomes femininos do arquivo de recursos.
     */
    private void carregarNomesFemininos() {
        try {
            nomesFemininos.addAll(carregarLista(CAMINHO_NOMES_FEMININOS));
            LOGGER.info("Carregados " + nomesFemininos.size() + " nomes femininos");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Erro ao carregar nomes femininos", e);
            // Adiciona alguns nomes padrão caso falhe
            nomesFemininos.addAll(Arrays.asList("Maria", "Ana", "Juliana", "Sofia", "Isabela"));
        }
    }
    
    /**
     * Carrega os sobrenomes do arquivo de recursos.
     */
    private void carregarSobrenomes() {
        try {
            sobrenomes.addAll(carregarLista(CAMINHO_SOBRENOMES));
            LOGGER.info("Carregados " + sobrenomes.size() + " sobrenomes");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Erro ao carregar sobrenomes", e);
            // Adiciona alguns sobrenomes padrão caso falhe
            sobrenomes.addAll(Arrays.asList("Silva", "Santos", "Oliveira", "Souza", "Lima"));
        }
    }
    
    /**
     * Carrega uma lista de strings a partir de um arquivo.
     * 
     * @param caminhoArquivo Caminho do arquivo de recursos
     * @return Lista de itens separados por vírgula no arquivo
     * @throws IOException Em caso de erro de leitura
     */
    private List<String> carregarLista(String caminhoArquivo) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(caminhoArquivo)) {
            if (is == null) {
                LOGGER.warning("Arquivo não encontrado: " + caminhoArquivo);
                throw new IOException("Arquivo não encontrado: " + caminhoArquivo);
            }
            
            byte[] bytes = is.readAllBytes();
            String conteudo = new String(bytes, StandardCharsets.UTF_8);
            
            return Arrays.stream(conteudo.split(","))
                   .map(String::trim)
                   .filter(s -> !s.isEmpty())
                   .collect(Collectors.toList());
        }
    }
    
    /**
     * Obtém um nome masculino aleatório.
     * 
     * @return Nome masculino aleatório
     */
    public String obterNomeMasculinoAleatorio() {
        if (nomesMasculinos.isEmpty()) {
            return "João"; // Nome padrão se a lista estiver vazia
        }
        return nomesMasculinos.get(random.nextInt(nomesMasculinos.size()));
    }
    
    /**
     * Obtém um nome feminino aleatório.
     * 
     * @return Nome feminino aleatório
     */
    public String obterNomeFemininoAleatorio() {
        if (nomesFemininos.isEmpty()) {
            return "Maria"; // Nome padrão se a lista estiver vazia
        }
        return nomesFemininos.get(random.nextInt(nomesFemininos.size()));
    }
    
    /**
     * Obtém um sobrenome aleatório.
     * 
     * @return Sobrenome aleatório
     */
    public String obterSobrenomeAleatorio() {
        if (sobrenomes.isEmpty()) {
            return "Silva"; // Sobrenome padrão se a lista estiver vazia
        }
        return sobrenomes.get(random.nextInt(sobrenomes.size()));
    }
}