package br.com.braym.projetobitlifeclonev1.utils;

import br.com.braym.projetobitlifeclonev1.domain.Consequencia;
import br.com.braym.projetobitlifeclonev1.domain.ConsequenciaGrupo;
import br.com.braym.projetobitlifeclonev1.domain.Evento;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Classe unificada para leitura de arquivos JSON.
 * Substitui as classes EventoJSONReader, LeitorEventoJSON e ConsequenciaJSONReader.
 */
public class LeitorJSON {
    private static final Logger LOGGER = Logger.getLogger(LeitorJSON.class.getName());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Lê eventos de um arquivo JSON
     * @param caminhoArquivo Caminho do arquivo
     * @return Lista de eventos ou lista vazia em caso de erro
     */
    public List<Evento> lerEventos(String caminhoArquivo) {
        try {
            // Tenta primeiro encontrar como arquivo no sistema de arquivos
            Path path = Paths.get(caminhoArquivo);
            if (Files.exists(path)) {
                return lerEventosDeArquivo(path);
            }

            // Se não encontrar, tenta carregar como recurso
            return lerEventosDeRecurso(caminhoArquivo);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao ler eventos do arquivo: " + caminhoArquivo, e);
            return new ArrayList<>();
        }
    }

    /**
     * Lê consequências de um arquivo JSON
     * @param nomeArquivo Nome do arquivo
     * @return Mapa com ID do grupo e lista de consequências
     */
    public Map<Integer, List<Consequencia>> lerConsequencias(String nomeArquivo) {
        try {
            // Tenta primeiro encontrar como arquivo no sistema de arquivos
            Path path = Paths.get(nomeArquivo);
            if (Files.exists(path)) {
                return lerConsequenciasDeArquivo(path);
            }

            // Se não encontrar, tenta carregar como recurso
            return lerConsequenciasDeRecurso(nomeArquivo);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao ler consequências do arquivo: " + nomeArquivo, e);
            return null;
        }
    }

    /**
     * Lê um evento único de um arquivo JSON
     * @param caminhoArquivo Caminho do arquivo
     * @return Evento ou null em caso de erro
     */
    public Evento lerEvento(String caminhoArquivo) {
        try {
            Path path = Paths.get(caminhoArquivo);
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    Evento evento = GSON.fromJson(reader, Evento.class);
                    return evento != null && evento.isValido() ? evento : null;
                }
            }

            // Tenta como recurso
            try (InputStream is = getClass().getResourceAsStream("/" + caminhoArquivo)) {
                if (is == null) {
                    throw new FileNotFoundException("Arquivo não encontrado: " + caminhoArquivo);
                }
                try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    Evento evento = GSON.fromJson(reader, Evento.class);
                    return evento != null && evento.isValido() ? evento : null;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao ler evento do arquivo: " + caminhoArquivo, e);
        }
        return null;
    }

    // Métodos auxiliares privados

    private List<Evento> lerEventosDeArquivo(Path caminho) {
        try (Reader reader = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            return deserializarEventos(reader);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao ler eventos do arquivo: " + caminho, e);
            return new ArrayList<>();
        }
    }

    private List<Evento> lerEventosDeRecurso(String nomeRecurso) {
        try (InputStream is = getClass().getResourceAsStream("/" + nomeRecurso)) {
            if (is == null) {
                LOGGER.warning("Recurso não encontrado: " + nomeRecurso);
                return new ArrayList<>();
            }
            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                return deserializarEventos(reader);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao ler eventos do recurso: " + nomeRecurso, e);
            return new ArrayList<>();
        }
    }

    private List<Evento> deserializarEventos(Reader reader) {
        Type listaTipo = new TypeToken<List<Evento>>() {}.getType();
        List<Evento> eventos = GSON.fromJson(reader, listaTipo);
        if (eventos != null) {
            // Filtra eventos inválidos
            eventos.removeIf(evento -> {
                boolean valido = evento.isValido();
                if (!valido) {
                    LOGGER.warning("Evento inválido ignorado: " + evento.getId());
                }
                return !valido;
            });
            return eventos;
        }
        return new ArrayList<>();
    }

    private Map<Integer, List<Consequencia>> lerConsequenciasDeArquivo(Path caminho) {
        try (Reader reader = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            return deserializarConsequencias(reader);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao ler consequências do arquivo: " + caminho, e);
            return null;
        }
    }

    private Map<Integer, List<Consequencia>> lerConsequenciasDeRecurso(String nomeRecurso) {
        try (InputStream is = getClass().getResourceAsStream("/" + nomeRecurso)) {
            if (is == null) {
                LOGGER.warning("Recurso não encontrado: " + nomeRecurso);
                return null;
            }
            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                return deserializarConsequencias(reader);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao ler consequências do recurso: " + nomeRecurso, e);
            return null;
        }
    }

    private Map<Integer, List<Consequencia>> deserializarConsequencias(Reader reader) {
        Type grupoType = new TypeToken<List<ConsequenciaGrupo>>() {}.getType();
        List<ConsequenciaGrupo> grupos = GSON.fromJson(reader, grupoType);
        return grupos.stream().collect(Collectors.toMap(ConsequenciaGrupo::getId, ConsequenciaGrupo::getEfeitos));
    }
}