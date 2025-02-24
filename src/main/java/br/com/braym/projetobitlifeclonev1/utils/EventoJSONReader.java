package br.com.braym.projetobitlifeclonev1.utils;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário para leitura de eventos a partir de um arquivo JSON.
 */
public class EventoJSONReader {

    /**
     * Lê e retorna a lista de eventos do arquivo JSON especificado.
     * @param nomeArquivo o nome do arquivo JSON (na raiz dos recursos)
     * @return lista de eventos; se não encontrado, retorna uma lista vazia
     */
    public static List<Evento> lerEventos(String nomeArquivo) {
        Gson gson = new Gson();
        Type listaTipo = new TypeToken<List<Evento>>() {}.getType();
        InputStream is = EventoJSONReader.class.getResourceAsStream("/" + nomeArquivo);
        if (is == null) {
            System.err.println("Arquivo " + nomeArquivo + " não encontrado!");
            return new ArrayList<>();
        }
        InputStreamReader reader = new InputStreamReader(is);
        return gson.fromJson(reader, listaTipo);
    }
}
