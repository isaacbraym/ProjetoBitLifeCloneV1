package br.com.braym.projetobitlifeclonev1.infrastructure.persistence;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Gerencia a persistência do progresso do jogo, permitindo salvar e carregar o estado do personagem.
 */
public class GameSaveManager {
    private static final Logger LOGGER = Logger.getLogger(GameSaveManager.class.getName());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Salva o estado do personagem em um arquivo JSON.
     * @param personagem o personagem a ser salvo
     * @param filePath o caminho do arquivo de salvamento
     */
    public static void saveGame(Personagem personagem, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            GSON.toJson(personagem, writer);
            LOGGER.info("Jogo salvo com sucesso em: " + filePath);
        } catch (IOException e) {
            LOGGER.severe("Erro ao salvar o jogo: " + e.getMessage());
        }
    }

    /**
     * Carrega o estado do personagem a partir de um arquivo JSON.
     * @param filePath o caminho do arquivo de salvamento
     * @return o personagem carregado ou null se ocorrer um erro
     */
    public static Personagem loadGame(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Personagem personagem = GSON.fromJson(reader, Personagem.class);
            LOGGER.info("Jogo carregado com sucesso de: " + filePath);
            return personagem;
        } catch (IOException e) {
            LOGGER.severe("Erro ao carregar o jogo: " + e.getMessage());
            return null;
        }
    }
}
