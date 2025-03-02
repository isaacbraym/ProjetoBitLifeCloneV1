package br.com.braym.projetobitlifeclonev1.domain;

import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;
import br.com.braym.projetobitlifeclonev1.interfaces.Observador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Representa um personagem no jogo com seus atributos e comportamentos.
 * Implementa o padrão Observer para notificar sobre alterações nos atributos.
 */
public class Personagem {
    private static final Logger LOGGER = Logger.getLogger(Personagem.class.getName());
    private static final int LIMITE_MINIMO_ATRIBUTO = 0;
    private static final int LIMITE_MAXIMO_ATRIBUTO = 100;
    private static final int VALOR_PADRAO_ATRIBUTO = 50;
    private static final int VALOR_MAXIMO_SAUDE = 100;

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

    /**
     * Construtor do personagem com nome
     * @param nome Nome do personagem
     */
    public Personagem(String nome) {
        this.nome = nome;
        this.idade = 0;
        inicializarAtributos();
        // Estado inicial definido como Infância
        this.estadoVida = new br.com.braym.projetobitlifeclonev1.impl.Infancia();
    }
    
    /**
     * Inicializa os atributos do personagem com valores padrão
     */
    private void inicializarAtributos() {
        this.aparencia = VALOR_PADRAO_ATRIBUTO;
        this.saude = VALOR_MAXIMO_SAUDE;
        this.sanidade = VALOR_MAXIMO_SAUDE;
        this.felicidade = VALOR_PADRAO_ATRIBUTO;
        this.inteligencia = VALOR_PADRAO_ATRIBUTO;
        this.carisma = VALOR_PADRAO_ATRIBUTO;
        this.financas = 0;
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
        this.aparencia = limitarAtributo(aparencia);
        notificarObservadores("Aparência atualizada para: " + this.aparencia);
    }

    public int getSaude() {
        return saude;
    }

    public void setSaude(int saude) {
        this.saude = limitarAtributo(saude);
        notificarObservadores("Saúde atualizada para: " + this.saude);
    }

    public int getSanidade() {
        return sanidade;
    }

    public void setSanidade(int sanidade) {
        this.sanidade = limitarAtributo(sanidade);
        notificarObservadores("Sanidade atualizada para: " + this.sanidade);
    }

    public int getFelicidade() {
        return felicidade;
    }

    public void setFelicidade(int felicidade) {
        this.felicidade = limitarAtributo(felicidade);
        notificarObservadores("Felicidade atualizada para: " + this.felicidade);
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = limitarAtributo(inteligencia);
        notificarObservadores("Inteligência atualizada para: " + this.inteligencia);
    }

    public int getCarisma() {
        return carisma;
    }

    public void setCarisma(int carisma) {
        this.carisma = limitarAtributo(carisma);
        notificarObservadores("Carisma atualizado para: " + this.carisma);
    }

    public int getFinancas() {
        return financas;
    }

    public void setFinancas(int financas) {
        this.financas = financas;
        notificarObservadores("Finanças atualizadas para: " + this.financas);
    }

    // Métodos para alteração de atributos
    /**
     * Altera o valor de finanças adicionando ou subtraindo um delta
     * @param delta Valor a ser adicionado (ou subtraído se negativo)
     */
    public void alterarFinancas(int delta) {
        setFinancas(getFinancas() + delta);
    }

    /**
     * Altera o valor de inteligência adicionando ou subtraindo um delta
     * @param delta Valor a ser adicionado (ou subtraído se negativo)
     */
    public void alterarInteligencia(int delta) {
        setInteligencia(getInteligencia() + delta);
    }

    /**
     * Altera o valor de felicidade adicionando ou subtraindo um delta
     * @param delta Valor a ser adicionado (ou subtraído se negativo)
     */
    public void alterarFelicidade(int delta) {
        setFelicidade(getFelicidade() + delta);
    }

    /**
     * Altera o valor de sanidade adicionando ou subtraindo um delta
     * @param delta Valor a ser adicionado (ou subtraído se negativo)
     */
    public void alterarSanidade(int delta) {
        setSanidade(getSanidade() + delta);
    }
    
    /**
     * Altera o valor de saúde adicionando ou subtraindo um delta
     * @param delta Valor a ser adicionado (ou subtraído se negativo)
     */
    public void alterarSaude(int delta) {
        setSaude(getSaude() + delta);
    }
    
    /**
     * Altera o valor de carisma adicionando ou subtraindo um delta
     * @param delta Valor a ser adicionado (ou subtraído se negativo)
     */
    public void alterarCarisma(int delta) {
        setCarisma(getCarisma() + delta);
    }
    
    /**
     * Altera o valor de aparência adicionando ou subtraindo um delta
     * @param delta Valor a ser adicionado (ou subtraído se negativo)
     */
    public void alterarAparencia(int delta) {
        setAparencia(getAparencia() + delta);
    }
    
    /**
     * Altera múltiplos atributos do personagem
     * @param efeitos Mapa contendo o nome do atributo e o valor a ser alterado
     */
    public void alterarAtributos(Map<String, Integer> efeitos) {
        if (efeitos == null) return;
        
        efeitos.forEach((atributo, valor) -> {
            switch (atributo.toLowerCase()) {
                case "financas" -> alterarFinancas(valor);
                case "felicidade" -> alterarFelicidade(valor);
                case "sanidade" -> alterarSanidade(valor);
                case "saude" -> alterarSaude(valor);
                case "inteligencia" -> alterarInteligencia(valor);
                case "carisma" -> alterarCarisma(valor);
                case "aparencia" -> alterarAparencia(valor);
                default -> LOGGER.warning("Atributo desconhecido: " + atributo);
            }
        });
    }

    // Gerenciamento de observadores
    /**
     * Adiciona um observador para receber notificações de alterações
     * @param observador Observador a ser adicionado
     */
    public void adicionarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    /**
     * Remove um observador da lista de notificações
     * @param observador Observador a ser removido
     */
    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica todos os observadores com uma mensagem
     * @param mensagem Mensagem a ser enviada aos observadores
     */
    private void notificarObservadores(String mensagem) {
        for (Observador obs : observadores) {
            obs.atualizar(mensagem);
        }
    }

    /**
     * Simula o envelhecimento do personagem, incrementando a idade e atualizando atributos
     * @return A idade anterior do personagem
     */
    public int envelhecer() {
        int idadeAnterior = idade;
        setIdade(getIdade() + 1);
        alterarSaude(-1); // Cada ano diminui 1 ponto de saúde
        notificarObservadores("Envelhecimento: idade = " + getIdade());
        atualizarEstadoVida();
        return idadeAnterior;
    }

    /**
     * Atualiza o estado de vida do personagem utilizando o padrão State
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
     * Retorna o estado de vida atual do personagem
     * @return O estado de vida atual
     */
    public EstadoVida getEstadoVida() {
        return estadoVida;
    }

    /**
     * Define o estado de vida do personagem
     * @param estadoVida Novo estado de vida
     */
    public void setEstadoVida(EstadoVida estadoVida) {
        if (estadoVida == null) {
            throw new IllegalArgumentException("Estado de vida não pode ser nulo");
        }
        this.estadoVida = estadoVida;
        notificarObservadores("Estado de vida definido como: " + estadoVida.getEstado());
    }

    /**
     * Limita um valor para ficar dentro do intervalo de atributos permitido
     * @param valor Valor a ser limitado
     * @return Valor limitado entre o mínimo e máximo
     */
    private int limitarAtributo(int valor) {
        return Math.max(LIMITE_MINIMO_ATRIBUTO, Math.min(LIMITE_MAXIMO_ATRIBUTO, valor));
    }
}