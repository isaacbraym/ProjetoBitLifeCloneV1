package br.com.braym.projetobitlifeclonev1.infrastructure.persistence;

import br.com.braym.projetobitlifeclonev1.domain.EstadoVidaImpl;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.impl.ConsoleObservador;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;
import br.com.braym.projetobitlifeclonev1.interfaces.Observador;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVida;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Gerencia a persistência do progresso do jogo, permitindo salvar e carregar o estado do personagem.
 * Usa o formato JSON para serialização/deserialização.
 */
public class GerenciadorSalvamentoJogo {
    private static final Logger LOGGER = Logger.getLogger(GerenciadorSalvamentoJogo.class.getName());
    private static final String EXTENSAO_ARQUIVO = ".json";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("ddMMyyyy");
    
    // Pasta padrão para salvamentos
    private static final String PASTA_PADRAO_SALVAMENTOS = "C:\\Users\\Dell\\Downloads\\TestesSaveGame";
    
    private final Gson gson;
    
    /**
     * Construtor que inicializa o Gson configurado com adaptadores personalizados
     */
    public GerenciadorSalvamentoJogo() {
        this.gson = configurarGson();
        // Garante que a pasta de salvamentos existe
        criarPastaSalvamentosSeNecessario();
    }
    
    /**
     * Cria a pasta de salvamentos se não existir
     */
    private void criarPastaSalvamentosSeNecessario() {
        try {
            Path diretorio = Paths.get(PASTA_PADRAO_SALVAMENTOS);
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
                LOGGER.info("Pasta de salvamentos criada: " + PASTA_PADRAO_SALVAMENTOS);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao criar pasta de salvamentos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Configura o Gson com adaptadores personalizados para interfaces
     * @return Instância configurada do Gson
     */
    private Gson configurarGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                // Adaptador para a interface Observador
                .registerTypeAdapter(Observador.class, new ObservadorAdapter())
                // Adaptador para a interface EstadoVida
                .registerTypeAdapter(EstadoVida.class, new EstadoVidaAdapter())
                // Adaptadores para Random e suas subclasses
                .registerTypeAdapter(Random.class, new RandomAdapter())
                .registerTypeAdapter(ThreadLocalRandom.class, new RandomAdapter())
                .registerTypeAdapter(SecureRandom.class, new RandomAdapter())
                .create();
    }
    
    /**
     * Salva o estado do personagem automaticamente com nome padronizado
     * @param personagem Personagem a ser salvo
     * @return true se o salvamento foi bem-sucedido
     */
    public boolean salvarJogo(Personagem personagem) {
        if (personagem == null) {
            LOGGER.warning("Tentativa de salvar personagem nulo");
            return false;
        }
        
        String nomeArquivo = gerarNomeArquivo(personagem);
        String caminhoCompleto = Paths.get(PASTA_PADRAO_SALVAMENTOS, nomeArquivo).toString();
        
        try {
            try (FileWriter writer = new FileWriter(caminhoCompleto)) {
                gson.toJson(personagem, writer);
                LOGGER.info("Jogo salvo com sucesso em: " + caminhoCompleto);
                return true;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar o jogo: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Gera o nome do arquivo baseado no personagem e data atual
     * @param personagem Personagem a ser salvo
     * @return Nome do arquivo no formato nomePersonagem_idadePersonagem_dataSalvamento.json
     */
    private String gerarNomeArquivo(Personagem personagem) {
        String dataFormatada = LocalDateTime.now().format(FORMATO_DATA);
        return String.format("%s_%d_%s%s", 
                personagem.getNome(), 
                personagem.getIdade(), 
                dataFormatada,
                EXTENSAO_ARQUIVO);
    }
    
    /**
     * Lista todos os jogos salvos
     * @return Lista de informações sobre os jogos salvos
     */
    public List<DadosJogoSalvo> listarJogosSalvos() {
        List<DadosJogoSalvo> jogos = new ArrayList<>();
        
        try {
            File pasta = new File(PASTA_PADRAO_SALVAMENTOS);
            File[] arquivos = pasta.listFiles((dir, name) -> name.toLowerCase().endsWith(EXTENSAO_ARQUIVO));
            
            if (arquivos != null) {
                jogos = Arrays.stream(arquivos)
                        .map(this::extrairDadosDoArquivo)
                        .filter(dados -> dados != null)
                        .sorted(Comparator.comparing(DadosJogoSalvo::getDataSalvamento).reversed())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao listar jogos salvos: " + e.getMessage(), e);
        }
        
        return jogos;
    }
    
    /**
     * Extrai os dados de um arquivo de jogo salvo
     * @param arquivo Arquivo de jogo salvo
     * @return Objeto contendo os dados do jogo salvo
     */
    private DadosJogoSalvo extrairDadosDoArquivo(File arquivo) {
        try {
            String nomeArquivo = arquivo.getName();
            
            // Remove a extensão
            String nomeSemExtensao = nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'));
            
            // Parse das partes do nome
            String[] partes = nomeSemExtensao.split("_");
            if (partes.length >= 3) {
                String nomePersonagem = partes[0];
                int idade = Integer.parseInt(partes[1]);
                String dataSalvamento = partes[2];
                
                return new DadosJogoSalvo(
                        arquivo.getAbsolutePath(),
                        nomePersonagem,
                        idade,
                        dataSalvamento,
                        arquivo.lastModified()
                );
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao processar arquivo: " + arquivo.getName(), e);
        }
        
        return null;
    }
    
    /**
     * Carrega um personagem a partir de um arquivo específico
     * @param caminhoArquivo Caminho completo do arquivo
     * @return Personagem carregado ou null em caso de falha
     */
    public Personagem carregarJogo(String caminhoArquivo) {
        try {
            File arquivo = new File(caminhoArquivo);
            if (!arquivo.exists()) {
                LOGGER.warning("Arquivo não existe: " + caminhoArquivo);
                return null;
            }
            
            try (FileReader reader = new FileReader(arquivo)) {
                Personagem personagem = gson.fromJson(reader, Personagem.class);
                if (personagem != null) {
                    // Reinicializa observadores que não são serializados corretamente
                    personagem.adicionarObservador(new ConsoleObservador());
                    LOGGER.info("Jogo carregado com sucesso de: " + caminhoArquivo);
                    return personagem;
                }
            }
        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Erro de formato no arquivo: " + e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao ler arquivo: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Representa os dados de um jogo salvo
     */
    public static class DadosJogoSalvo {
        private final String caminhoArquivo;
        private final String nomePersonagem;
        private final int idade;
        private final String dataSalvamento;
        private final long timestampSalvamento;
        
        public DadosJogoSalvo(String caminhoArquivo, String nomePersonagem, int idade, 
                             String dataSalvamento, long timestampSalvamento) {
            this.caminhoArquivo = caminhoArquivo;
            this.nomePersonagem = nomePersonagem;
            this.idade = idade;
            this.dataSalvamento = dataSalvamento;
            this.timestampSalvamento = timestampSalvamento;
        }
        
        public String getCaminhoArquivo() {
            return caminhoArquivo;
        }
        
        public String getNomePersonagem() {
            return nomePersonagem;
        }
        
        public int getIdade() {
            return idade;
        }
        
        public String getDataSalvamento() {
            return dataSalvamento;
        }
        
        public long getTimestampSalvamento() {
            return timestampSalvamento;
        }
        
        @Override
        public String toString() {
            return String.format("%s_%d_%s", nomePersonagem, idade, dataSalvamento);
        }
    }
    
    /**
     * Adaptador para serialização/deserialização da interface Observador
     */
    private static class ObservadorAdapter implements JsonSerializer<Observador>, JsonDeserializer<Observador> {
        @Override
        public JsonElement serialize(Observador src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("type", new JsonPrimitive(src.getClass().getName()));
            return result;
        }
        
        @Override
        public Observador deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            // Sempre retorna uma nova instância de ConsoleObservador
            return new ConsoleObservador();
        }
    }
    
    /**
     * Adaptador para serialização/deserialização de objetos Random
     */
    private static class RandomAdapter implements JsonSerializer<Random>, JsonDeserializer<Random> {
        @Override
        public JsonElement serialize(Random src, Type typeOfSrc, JsonSerializationContext context) {
            // Não tenta serializar os campos internos do Random
            return new JsonObject();
        }
        
        @Override
        public Random deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            // Cria uma nova instância de Random
            return new Random();
        }
    }
    
    /**
     * Adaptador para serialização/deserialização da interface EstadoVida
     */
    private static class EstadoVidaAdapter implements JsonSerializer<EstadoVida>, JsonDeserializer<EstadoVida> {
        @Override
        public JsonElement serialize(EstadoVida src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("type", new JsonPrimitive(src.getClass().getName()));
            result.add("estado", new JsonPrimitive(src.getEstado()));
            return result;
        }
        
        @Override
        public EstadoVida deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String estado = jsonObject.get("estado").getAsString();
            
            // Baseado no estado, retorna a instância apropriada usando EstadoVidaImpl
            return switch (estado) {
                case "INFANCIA" -> new EstadoVidaImpl(FaseDaVida.INFANCIA);
                case "ADOLESCENCIA" -> new EstadoVidaImpl(FaseDaVida.ADOLESCENCIA);
                case "ADULTO" -> new EstadoVidaImpl(FaseDaVida.ADULTO);
                case "VELHICE" -> new EstadoVidaImpl(FaseDaVida.VELHICE);
                default -> new EstadoVidaImpl(FaseDaVida.INFANCIA); // Fallback para infância
            };
        }
    }
}