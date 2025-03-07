package br.com.braym.projetobitlifeclonev1.domain;

import java.io.Serializable;

/**
 * Representa um relacionamento com outra pessoa
 */
public class Relacionamento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Pessoa pessoa;
    private TipoRelacionamento tipo;
    private int nivel; // 0-100, quão forte é o relacionamento
    private int tempo; // tempo em anos
    
    public Relacionamento(Pessoa pessoa, TipoRelacionamento tipo) {
        this.pessoa = pessoa;
        this.tipo = tipo;
        this.nivel = 50; // nível inicial médio
        this.tempo = 0;
    }
    
    // Getters e setters
    public Pessoa getPessoa() { return pessoa; }
    public TipoRelacionamento getTipo() { return tipo; }
    public int getNivel() { return nivel; }
    public int getTempo() { return tempo; }
    
    public void setTipo(TipoRelacionamento tipo) {
        this.tipo = tipo;
    }
    
    public void setNivel(int nivel) {
        this.nivel = Math.max(0, Math.min(100, nivel));
    }
    
    /**
     * Altera o nível do relacionamento
     * @param delta Quantidade a alterar
     */
    public void alterarNivel(int delta) {
        setNivel(nivel + delta);
    }
    
    /**
     * Avança o tempo do relacionamento em um ano
     */
    public void avancarTempo() {
        tempo++;
        // Relacionamentos têm uma chance de degradar com o tempo se não forem mantidos
        if (Math.random() > 0.7) {
            alterarNivel(-5);
        }
    }
    
    /**
     * Retorna a descrição do relacionamento
     */
    public String getDescricao() {
        String qualidade = "neutro";
        if (nivel > 80) qualidade = "excelente";
        else if (nivel > 60) qualidade = "bom";
        else if (nivel < 30) qualidade = "ruim";
        else if (nivel < 10) qualidade = "péssimo";
        
        return pessoa.getNome() + " - " + tipo.getDescricao() + 
               " (" + qualidade + ", " + tempo + " anos)";
    }
}