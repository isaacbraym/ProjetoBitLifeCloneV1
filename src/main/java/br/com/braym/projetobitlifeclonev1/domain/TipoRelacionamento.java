package br.com.braym.projetobitlifeclonev1.domain;

/**
 * Tipos de relacionamentos possíveis no jogo
 */
public enum TipoRelacionamento {
    AMIZADE("Amigo(a)"),
    NAMORO("Namorado(a)"),
    CASAMENTO("Cônjuge"),
    PAI("Pai"),
    MAE("Mãe"),
    FILHO("Filho(a)"),
    IRMAO("Irmão/Irmã"),
    COLEGA("Colega");
    
    private final String descricao;
    
    TipoRelacionamento(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}