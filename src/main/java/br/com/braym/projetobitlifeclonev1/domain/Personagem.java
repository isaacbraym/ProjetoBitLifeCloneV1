package br.com.braym.projetobitlifeclonev1.domain;

import br.com.braym.projetobitlifeclonev1.interfaces.Observador;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Representa um personagem no jogo com atributos e comportamentos, incluindo a gestão de estados de vida.
 */
public class Personagem {
    private static final Logger LOGGER = Logger.getLogger(Personagem.class.getName());

    private String nome;
    private int idade;

    // Atributos com valores limitados de 0 a 100
    private int aparencia;
    private int saude;
    private int sanidade;
    private int felicidade;
    private int inteligencia;
    private int carisma;
    private int financas;

    // Lista de observadores para notificações
    private final List<Observador> observadores = new ArrayList<>();

    // Estado de vida atual (padrão State)
    private EstadoVida estadoVida;

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
        // Estado inicial definido como Infância
        this.estadoVida = new br.com.braym.projetobitlifeclonev1.impl.Infancia();
    }

    // Getters e Setters com notificações
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
        this.financas = financas;
        notificarObservadores("Finanças atualizadas para: " + this.financas);
    }

    // Gerenciamento de observadores
    public void adicionarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores(String mensagem) {
        for (Observador obs : observadores) {
            obs.atualizar(mensagem);
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
     * Simula o envelhecimento do personagem, incrementando a idade, alterando a saúde e atualizando o estado.
     */
    public void envelhecer() {
        setIdade(getIdade() + 1);
        setSaude(getSaude() - 1);
        notificarObservadores("Envelhecimento: idade = " + getIdade());
        atualizarEstadoVida();
    }

    /**
     * Atualiza o estado de vida do personagem utilizando o padrão State.
     */
    public void atualizarEstadoVida() {
        EstadoVida novoEstado = estadoVida.proximoEstado(this);
        if (!novoEstado.getEstado().equals(estadoVida.getEstado())) {
            LOGGER.info("Transição de estado: de " + estadoVida.getEstado() + " para " + novoEstado.getEstado());
            estadoVida = novoEstado;
            notificarObservadores("Estado de vida alterado para: " + estadoVida.getEstado());
        }
    }

    /**
     * Retorna o estado de vida atual do personagem.
     * @return o estado de vida
     */
    public EstadoVida getEstadoVida() {
        return estadoVida;
    }

    /**
     * Define o estado de vida atual do personagem.
     * @param estadoVida novo estado de vida
     */
    public void setEstadoVida(EstadoVida estadoVida) {
        this.estadoVida = estadoVida;
    }

    /**
     * Garante que um valor esteja dentro do intervalo [0, 100].
     * @param valor valor a ser ajustado
     * @return valor ajustado
     */
    private int clamp(int valor) {
        return Math.max(0, Math.min(100, valor));
    }
}
