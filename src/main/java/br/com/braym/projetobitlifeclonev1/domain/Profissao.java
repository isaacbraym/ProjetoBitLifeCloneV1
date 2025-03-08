package br.com.braym.projetobitlifeclonev1.domain;

/**
 * Representa uma profissão com seus atributos e faixas salariais
 */
public class Profissao {
    private String nome;
    private int salarioMinimo;
    private int salarioMaximo;
    private int financasMinimo;
    private int financasMaximo;
    
    /**
     * Construtor padrão
     */
    public Profissao() {
    }
    
    /**
     * Construtor completo
     * 
     * @param nome Nome da profissão
     * @param salarioMinimo Salário mínimo em R$
     * @param salarioMaximo Salário máximo em R$
     * @param financasMinimo Finanças mínimas iniciais em R$
     * @param financasMaximo Finanças máximas iniciais em R$
     */
    public Profissao(String nome, int salarioMinimo, int salarioMaximo, int financasMinimo, int financasMaximo) {
        this.nome = nome;
        this.salarioMinimo = salarioMinimo;
        this.salarioMaximo = salarioMaximo;
        this.financasMinimo = financasMinimo;
        this.financasMaximo = financasMaximo;
    }
    
    // Getters e Setters
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public int getSalarioMinimo() {
        return salarioMinimo;
    }
    
    public void setSalarioMinimo(int salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }
    
    public int getSalarioMaximo() {
        return salarioMaximo;
    }
    
    public void setSalarioMaximo(int salarioMaximo) {
        this.salarioMaximo = salarioMaximo;
    }
    
    public int getFinancasMinimo() {
        return financasMinimo;
    }
    
    public void setFinancasMinimo(int financasMinimo) {
        this.financasMinimo = financasMinimo;
    }
    
    public int getFinancasMaximo() {
        return financasMaximo;
    }
    
    public void setFinancasMaximo(int financasMaximo) {
        this.financasMaximo = financasMaximo;
    }
    
    @Override
    public String toString() {
        return nome + " (Salário: R$" + salarioMinimo + ",00 - R$" + salarioMaximo + ",00)";
    }
}