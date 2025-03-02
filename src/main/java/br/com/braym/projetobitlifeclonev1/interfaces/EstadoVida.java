package br.com.braym.projetobitlifeclonev1.interfaces;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;

/**
 * Interface que define o comportamento dos estados de vida do personagem usando
 * o padrão State.
 */
public interface EstadoVida {

	/**
	 * Determina e retorna o próximo estado de vida com base na situação atual do
	 * personagem.
	 * 
	 * @param personagem o personagem cuja idade e atributos são avaliados
	 * @return o próximo estado de vida
	 */
	EstadoVida proximoEstado(Personagem personagem);

	/**
	 * Retorna uma representação textual do estado.
	 * 
	 * @return o nome do estado
	 */
	String getEstado();

	/**
	 * Retorna uma descrição mais detalhada do estado de vida
	 * 
	 * @return descrição do estado
	 */
	default String getDescricao() {
		return "Estado de vida: " + getEstado();
	}

	/**
	 * Verifica se é possível transitar para um novo estado
	 * 
	 * @param personagem Personagem a ser verificado
	 * @return true se uma transição é possível, false caso contrário
	 */
	default boolean podeTransitar(Personagem personagem) {
		return !proximoEstado(personagem).getEstado().equals(getEstado());
	}
}