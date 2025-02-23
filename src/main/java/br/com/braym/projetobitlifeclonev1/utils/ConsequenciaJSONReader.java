package br.com.braym.projetobitlifeclonev1.utils;

import br.com.braym.projetobitlifeclonev1.consequencias.Consequencia;
import br.com.braym.projetobitlifeclonev1.consequencias.ConsequenciaGrupo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utilitário para leitura de consequências a partir de um arquivo JSON.
 */
public class ConsequenciaJSONReader {

    /**
     * Lê as consequências do arquivo JSON e as organiza em um mapa.
     * @param nomeArquivo o nome do arquivo JSON (na raiz dos recursos)
     * @return mapa onde a chave é o ID do grupo e o valor é a lista de consequências
     */
    public static Map<Integer, List<Consequencia>> lerConsequencias(String nomeArquivo) {
        Gson gson = new Gson();
        InputStream is = ConsequenciaJSONReader.class.getResourceAsStream("/" + nomeArquivo);
        if (is == null) {
            System.err.println("Arquivo " + nomeArquivo + " não encontrado!");
            return null;
        }
        InputStreamReader reader = new InputStreamReader(is);
        Type grupoType = new TypeToken<List<ConsequenciaGrupo>>() {}.getType();
        List<ConsequenciaGrupo> grupos = gson.fromJson(reader, grupoType);
        return grupos.stream().collect(Collectors.toMap(ConsequenciaGrupo::getId, ConsequenciaGrupo::getEfeitos));
    }
}
