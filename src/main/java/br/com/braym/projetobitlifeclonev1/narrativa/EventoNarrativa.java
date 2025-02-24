package br.com.braym.projetobitlifeclonev1.narrativa;

import java.util.List;

public class EventoNarrativa {
    private String id;              // Identificador único do evento
    private String descricao;       // Descrição do evento (narrativa)
    private String periodo;         // Período da vida em que o evento ocorre (ex.: "infancia", "adolescencia", "vida_adulta", "velhice")
    private List<Escolha> opcoes;   // Lista de opções disponíveis para o jogador

    public EventoNarrativa(String id, String descricao, String periodo, List<Escolha> opcoes) {
        this.id = id;
        this.descricao = descricao;
        this.periodo = periodo;
        this.opcoes = opcoes;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPeriodo() {
        return periodo;
    }

    public List<Escolha> getOpcoes() {
        return opcoes;
    }
}
