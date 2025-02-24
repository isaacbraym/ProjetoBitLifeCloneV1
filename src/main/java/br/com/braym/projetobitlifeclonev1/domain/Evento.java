package br.com.braym.projetobitlifeclonev1.domain;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Representa um evento no jogo, contendo descrição, opções, efeitos simples e múltiplos.
 */
public class Evento {
    private static final Logger LOGGER = Logger.getLogger(Evento.class.getName());

    private String id;
    private String descricao;
    private List<String> opcoes;
    private List<Integer> efeitos; // Efeitos correspondentes às opções
    private String atributo;    
    // Novo campo para suportar múltiplos efeitos (atributo -> valor)
    private Map<String, Integer> efeitosMultiplos;

    public Evento(String id, String descricao, List<String> opcoes, List<Integer> efeitos, String atributo) {
        this.id = id;
        this.descricao = descricao;
        this.opcoes = opcoes;
        this.efeitos = efeitos;
        this.atributo = atributo;
    }

    /**
     * Construtor padrão necessário para bibliotecas de deserialização, como o Gson.
     */
    public Evento() {
    }

    // Getters e Setters
    
    public String getId() {
        return id;
    }
    public String getDescricao() {
        return descricao;
    }

    public List<String> getOpcoes() {
        return opcoes;
    }

    public List<Integer> getEfeitos() {
        return efeitos;
    }

    public String getAtributo() {
        return atributo;
    }

    public Map<String, Integer> getEfeitosMultiplos() {
        return efeitosMultiplos;
    }

    public void setEfeitosMultiplos(Map<String, Integer> efeitosMultiplos) {
        this.efeitosMultiplos = efeitosMultiplos;
    }

    /**
     * Aplica o efeito de uma escolha para efeitos simples.
     * @param escolha índice da opção escolhida
     */
    public void aplicarEfeito(int escolha) {
        if (escolha >= 0 && escolha < efeitos.size()) {
            LOGGER.info("Aplicando efeito: " + efeitos.get(escolha));
        } else {
            LOGGER.warning("Escolha inválida.");
        }
    }
}
