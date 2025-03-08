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
    private Profissao profissao; // Nova propriedade para profissão
    private int salario;        // Salário atual
    
    /**
     * Construtor para Pessoa com sobrenome e profissão fornecidos
     * 
     * @param nome Nome da pessoa
     * @param sobrenome Sobrenome da pessoa
     * @param idade Idade da pessoa
     * @param genero Gênero da pessoa
     * @param profissao Profissão da pessoa
     */
    public Pessoa(String nome, String sobrenome, int idade, String genero, Profissao profissao) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.genero = genero;
        this.compatibilidade = Personagem.obterAleatorio(30, 100);
        this.felicidade = 50;
        this.profissao = profissao;
        
        // Define um salário aleatório dentro da faixa da profissão
        if (profissao != null) {
            this.salario = Personagem.obterAleatorio(profissao.getSalarioMinimo(), profissao.getSalarioMaximo());
        } else {
            this.salario = 0;
        }
    }
    
    /**
     * Construtor para Pessoa com sobrenome fornecido
     * @param nome Nome da pessoa
     * @param sobrenome Sobrenome da pessoa
     * @param idade Idade da pessoa
     * @param genero Gênero da pessoa
     */
    public Pessoa(String nome, String sobrenome, int idade, String genero) {
        this(nome, sobrenome, idade, genero, null);
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
        this.profissao = null;
        this.salario = 0;
    }
    
    // Getters e setters originais
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getSobrenome() { return sobrenome; }
    public String getNomeCompleto() { return nome + " " + sobrenome; }
    public int getIdade() { return idade; }
    public String getGenero() { return genero; }
    public int getCompatibilidade() { return compatibilidade; }
    public int getFelicidade() { return felicidade; }
    
    // Novos getters e setters para profissão e salário
    public Profissao getProfissao() { return profissao; }
    public void setProfissao(Profissao profissao) { this.profissao = profissao; }
    public int getSalario() { return salario; }
    public void setSalario(int salario) { this.salario = salario; }
    
    public void setFelicidade(int felicidade) {
        this.felicidade = Math.max(0, Math.min(100, felicidade));
    }
    
    public void envelhecer() {
        idade++;
    }
    
    @Override
    public String toString() {
        String info = nome + " " + sobrenome + " (" + idade + " anos)";
        if (profissao != null) {
            info += " - " + profissao.getNome() + " (R$" + salario + ",00)";
        }
        return info;
    }
}