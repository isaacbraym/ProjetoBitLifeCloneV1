package br.com.braym.projetobitlifeclonev1;

import br.com.braym.projetobitlifeclonev1.interfaces.Observador;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um personagem no jogo, com atributos e comportamento para atualização e notificação de mudanças.
 */
public class Personagem {
    private String nome;
    private int idade;

    // Atributos com valores limitados de 0 a 100
    private int aparencia;
    private int saude;
    private int sanidade;
    private int felicidade;
    private int inteligencia;
    private int carisma;
    private int financas; // Se necessário, aplicar limite com clamp

    private final List<Observador> observadores = new ArrayList<>();

    public Personagem(String nome) {
        this.nome = nome;
        this.idade = 0;
        this.aparencia = 50;
        this.saude = 100;
        this.sanidade = 100;
        this.felicidade = 50;
        this.inteligencia = 50;
        this.carisma = 50;
        this.financas = 0;
    }

    // Getters e Setters com notificação e aplicação do clamp quando aplicável

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        notificarObservadores("Nome alterado para: " + nome);
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
        notificarObservadores("Idade alterada para: " + idade);
    }

    public int getAparencia() {
        return aparencia;
    }

    public void setAparencia(int aparencia) {
        this.aparencia = clamp(aparencia);
        notificarObservadores("Aparência atualizada para: " + this.aparencia);
    }

    public int getSaude() {
        return saude;
    }

    public void setSaude(int saude) {
        this.saude = clamp(saude);
        notificarObservadores("Saúde atualizada para: " + this.saude);
    }

    public int getSanidade() {
        return sanidade;
    }

    public void setSanidade(int sanidade) {
        this.sanidade = clamp(sanidade);
        notificarObservadores("Sanidade atualizada para: " + this.sanidade);
    }

    public int getFelicidade() {
        return felicidade;
    }

    public void setFelicidade(int felicidade) {
        this.felicidade = clamp(felicidade);
        notificarObservadores("Felicidade atualizada para: " + this.felicidade);
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = clamp(inteligencia);
        notificarObservadores("Inteligência atualizada para: " + this.inteligencia);
    }

    public int getCarisma() {
        return carisma;
    }

    public void setCarisma(int carisma) {
        this.carisma = clamp(carisma);
        notificarObservadores("Carisma atualizado para: " + this.carisma);
    }

    public int getFinancas() {
        return financas;
    }

    public void setFinancas(int financas) {
        // Caso seja necessário limitar o valor, pode-se aplicar o clamp aqui
        this.financas = financas;
        notificarObservadores("Finanças atualizadas para: " + this.financas);
    }

    // Métodos para gerenciamento de observadores

    public void adicionarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores(String mensagem) {
        for (Observador observador : observadores) {
            observador.atualizar(mensagem);
        }
    }

    // Métodos para alteração de atributos

    public void alterarFinancas(int delta) {
        setFinancas(getFinancas() + delta);
    }

    public void alterarInteligencia(int delta) {
        setInteligencia(getInteligencia() + delta);
    }

    public void alterarFelicidade(int delta) {
        setFelicidade(getFelicidade() + delta);
    }

    public void alterarSanidade(int delta) {
        setSanidade(getSanidade() + delta);
    }

    /**
     * Simula o envelhecimento do personagem, incrementando a idade e alterando atributos.
     */
    public void envelhecer() {
        setIdade(getIdade() + 1);
        // Exemplo simples: diminui 1 ponto de saúde ao envelhecer
        setSaude(getSaude() - 1);
        notificarObservadores("Envelhecimento: idade = " + getIdade());
    }

    /**
     * Garante que um valor esteja dentro do intervalo [0, 100].
     * @param valor o valor a ser ajustado
     * @return o valor ajustado
     */
    private int clamp(int valor) {
        return Math.max(0, Math.min(100, valor));
    }
}
