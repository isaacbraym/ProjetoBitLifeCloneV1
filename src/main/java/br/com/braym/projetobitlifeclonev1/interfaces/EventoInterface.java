package br.com.braym.projetobitlifeclonev1.interfaces;

import br.com.braym.projetobitlifeclonev1.Personagem;

/**
 * Interface que define o contrato para execução de eventos que afetam o personagem.
 */
public interface EventoInterface {
	/**
	 * Executa o evento, alterando os atributos do personagem conforme necessário.
	 * 
	 * @param personagem o personagem a ser afetado pelo evento
	 */
	void executarEvento(Personagem personagem);
}
