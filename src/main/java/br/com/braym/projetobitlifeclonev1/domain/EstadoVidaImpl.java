package br.com.braym.projetobitlifeclonev1.domain;

import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVida;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;

/**
 * Implementação unificada dos estados de vida usando o padrão State.
 * Substitui tanto EstadoVidaBase quanto as implementações separadas.
 */
public class EstadoVidaImpl implements EstadoVida {
    
    private final FaseDaVida faseDaVida;
    private final String descricao;
    
    /**
     * Construtor padrão
     * @param faseDaVida Enum representando a fase da vida
     * @param descricao Descrição detalhada da fase
     */
    public EstadoVidaImpl(FaseDaVida faseDaVida, String descricao) {
        this.faseDaVida = faseDaVida;
        this.descricao = descricao;
    }
    
    /**
     * Construtor que usa a fase da vida para definir a descrição padrão
     * @param faseDaVida Enum representando a fase da vida
     */
    public EstadoVidaImpl(FaseDaVida faseDaVida) {
        this.faseDaVida = faseDaVida;
        this.descricao = getDescricaoPadrao(faseDaVida);
    }
    
    /**
     * Factory method para criar estado baseado na idade
     * @param idade Idade do personagem
     * @return Estado de vida apropriado
     */
    public static EstadoVidaImpl criarPorIdade(int idade) {
        FaseDaVida fase = FaseDaVidaResolver.obterFaseDaVida(idade);
        return new EstadoVidaImpl(fase);
    }
    
    @Override
    public EstadoVida proximoEstado(Personagem personagem) {
        FaseDaVida novaFase = FaseDaVidaResolver.obterFaseDaVida(personagem.getIdade());
        
        if (novaFase != this.faseDaVida) {
            return new EstadoVidaImpl(novaFase);
        }
        
        return this;
    }
    
    @Override
    public String getEstado() {
        return faseDaVida.toString();
    }
    
    @Override
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Obtém a descrição padrão para uma fase da vida
     * @param fase Enum da fase
     * @return Descrição textual detalhada
     */
    private String getDescricaoPadrao(FaseDaVida fase) {
        return switch (fase) {
            case INFANCIA -> "Período de desenvolvimento inicial, caracterizado por rápido crescimento físico e cognitivo.";
            case ADOLESCENCIA -> "Fase de transição entre a infância e a vida adulta, marcada por mudanças biológicas e psicológicas.";
            case ADULTO -> "Período de maturidade, caracterizado por independência, responsabilidades e estabilidade.";
            case VELHICE -> "Fase final da vida, caracterizada por mudanças físicas e sabedoria acumulada.";
        };
    }
    
    /**
     * Determina se o nome do estado é igual ao outro estado
     * @param outroEstado Estado a ser comparado
     * @return true se os estados têm o mesmo nome
     */
    public boolean mesmoEstadoQue(EstadoVida outroEstado) {
        return this.getEstado().equals(outroEstado.getEstado());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoVidaImpl that = (EstadoVidaImpl) o;
        return faseDaVida == that.faseDaVida;
    }
    
    @Override
    public int hashCode() {
        return faseDaVida.hashCode();
    }
    
    @Override
    public String toString() {
        return "EstadoVida{" +
                "faseDaVida=" + faseDaVida +
                '}';
    }
}