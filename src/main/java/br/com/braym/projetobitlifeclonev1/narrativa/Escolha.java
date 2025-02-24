package br.com.braym.projetobitlifeclonev1.narrativa;

import java.util.Map;

public class Escolha {
    private String texto;                // Texto apresentado para o jogador
    private String retornoNarrativo;     // Retorno narrativo após a escolha
    private Map<String, Integer> efeitos; // Efeitos nos atributos (ex: "financas": 10, "felicidade": -5)
    private String idProximoEvento;      // ID do próximo evento (pode ser null)

    public Escolha(String texto, String retornoNarrativo, Map<String, Integer> efeitos, String idProximoEvento) {
        this.texto = texto;
        this.retornoNarrativo = retornoNarrativo;
        this.efeitos = efeitos;
        this.idProximoEvento = idProximoEvento;
    }

    public String getTexto() {
        return texto;
    }

    public String getRetornoNarrativo() {
        return retornoNarrativo;
    }

    public Map<String, Integer> getEfeitos() {
        return efeitos;
    }

    public String getIdProximoEvento() {
        return idProximoEvento;
    }
}
