package br.com.braym.projetobitlifeclonev1.domain;

import java.util.UUID;

/**
 * Representa um personagem relacionado ao personagem principal
 */
public class Pessoa {
    private final String id;
    private String nome;
    private String sobrenome;
    private int idade;
    private String genero;
    private int compatibilidade; // 0-100, quanto maior mais compatível
    private int felicidade;      // 0-100, quão feliz está no relacionamento
    
    /**
     * Construtor para Pessoa com sobrenome fornecido
     * @param nome Nome da pessoa
     * @param sobrenome Sobrenome da pessoa
     * @param idade Idade da pessoa
     * @param genero Gênero da pessoa
     */
    public Pessoa(String nome, String sobrenome, int idade, String genero) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.genero = genero;
        this.compatibilidade = Personagem.obterAleatorio(30, 100);
        this.felicidade = 50;
    }
    
    /**
     * Construtor original para compatibilidade
     * @param nome Nome da pessoa
     * @param idade Idade da pessoa
     * @param genero Gênero da pessoa
     */
    public Pessoa(String nome, int idade, String genero) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.sobrenome = ""; // Sobrenome vazio por padrão
        this.idade = idade;
        this.genero = genero;
        this.compatibilidade = Personagem.obterAleatorio(30, 100);
        this.felicidade = 50;
    }
    
    // Getters e setters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getSobrenome() { return sobrenome; }
    public String getNomeCompleto() { return nome + " " + sobrenome; }
    public int getIdade() { return idade; }
    public String getGenero() { return genero; }
    public int getCompatibilidade() { return compatibilidade; }
    public int getFelicidade() { return felicidade; }
    
    public void setFelicidade(int felicidade) {
        this.felicidade = Math.max(0, Math.min(100, felicidade));
    }
    
    public void envelhecer() {
        idade++;
    }
    
    @Override
    public String toString() {
        return nome + " " + sobrenome + " (" + idade + " anos)";
    }
}