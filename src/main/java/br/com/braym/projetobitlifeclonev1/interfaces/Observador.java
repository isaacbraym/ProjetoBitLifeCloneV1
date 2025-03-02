package br.com.braym.projetobitlifeclonev1.interfaces;

/**
 * Interface do padrão Observer para receber notificações de alterações. Permite
 * que objetos interessados sejam notificados quando houver mudanças no objeto
 * observado.
 */
public interface Observador {

	/**
	 * Método chamado quando o objeto observado sofre uma alteração
	 * 
	 * @param mensagem Mensagem contendo detalhes sobre a alteração
	 */
	void atualizar(String mensagem);

	/**
	 * Método chamado quando o objeto observado sofre uma alteração, permitindo
	 * acesso ao objeto que foi modificado
	 * 
	 * @param mensagem Mensagem contendo detalhes sobre a alteração
	 * @param fonte    Objeto que gerou a notificação
	 */
	default void atualizar(String mensagem, Object fonte) {
		atualizar(mensagem);
	}
}