package br.com.braym.projetobitlifeclonev1.utils;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitário para leitura de eventos a partir de arquivos JSON. Usa recursos do
 * Java 17 e bibliotecas modernas para manipulação de JSON.
 */
public class LeitorEventoJSON {
	private static final Logger LOGGER = Logger.getLogger(LeitorEventoJSON.class.getName());
	private static final Gson GSON = new GsonBuilder().create();

	private LeitorEventoJSON() {
		// Construtor privado para evitar instanciação
	}

	/**
	 * Lê e retorna a lista de eventos do arquivo JSON especificado
	 * 
	 * @param caminhoArquivo Caminho do arquivo JSON
	 * @return Lista de eventos ou lista vazia em caso de erro
	 */
	public static List<Evento> lerEventos(String caminhoArquivo) {
		try {
			// Tenta primeiro carregar como arquivo no sistema de arquivos
			Path path = Paths.get(caminhoArquivo);
			if (Files.exists(path)) {
				return lerEventosDeArquivo(path);
			}

			// Se não encontrar, tenta carregar como recurso do classpath
			return lerEventosDeRecurso("/" + caminhoArquivo);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Erro ao ler eventos do arquivo: " + caminhoArquivo, e);
			return new ArrayList<>();
		}
	}

	/**
	 * Lê eventos de um arquivo no sistema de arquivos
	 * 
	 * @param caminho Caminho do arquivo
	 * @return Lista de eventos
	 */
	private static List<Evento> lerEventosDeArquivo(Path caminho) {
		try (Reader reader = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
			return deserializarEventos(reader);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Erro ao ler eventos do arquivo: " + caminho, e);
			return new ArrayList<>();
		}
	}

	/**
	 * Lê eventos de um recurso no classpath
	 * 
	 * @param nomeRecurso Nome do recurso
	 * @return Lista de eventos
	 */
	private static List<Evento> lerEventosDeRecurso(String nomeRecurso) {
		InputStream is = LeitorEventoJSON.class.getResourceAsStream(nomeRecurso);
		if (is == null) {
			LOGGER.warning("Recurso não encontrado: " + nomeRecurso);
			return new ArrayList<>();
		}

		try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
			return deserializarEventos(reader);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Erro ao ler eventos do recurso: " + nomeRecurso, e);
			return new ArrayList<>();
		}
	}

	/**
	 * Desserializa eventos de um Reader JSON
	 * 
	 * @param reader Reader contendo JSON
	 * @return Lista de eventos
	 */
	private static List<Evento> deserializarEventos(Reader reader) {
		Type tipoLista = new TypeToken<List<Evento>>() {
		}.getType();
		List<Evento> eventos = GSON.fromJson(reader, tipoLista);

		// Filtra eventos inválidos
		if (eventos != null) {
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

	/**
	 * Carrega um único evento de um arquivo JSON
	 * 
	 * @param caminhoArquivo Caminho do arquivo JSON
	 * @return Evento carregado ou null em caso de erro
	 */
	public static Evento lerEvento(String caminhoArquivo) {
		try {
			Path path = Paths.get(caminhoArquivo);
			if (Files.exists(path)) {
				try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
					Evento evento = GSON.fromJson(reader, Evento.class);
					return evento != null && evento.isValido() ? evento : null;
				}
			}

			InputStream is = LeitorEventoJSON.class.getResourceAsStream("/" + caminhoArquivo);
			if (is == null) {
				throw new FileNotFoundException("Arquivo/recurso não encontrado: " + caminhoArquivo);
			}

			try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
				Evento evento = GSON.fromJson(reader, Evento.class);
				return evento != null && evento.isValido() ? evento : null;
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Erro ao ler evento do arquivo: " + caminhoArquivo, e);
			return null;
		}
	}
}