package br.com.braym.projetobitlifeclonev1.interfaces;

/**
 * Interface do padrão Observer para receber notificações de alterações.
 */
public interface Observador {
    /**
     * Atualiza o observador com a mensagem informada.
     * @param mensagem a mensagem de notificação
     */
    void atualizar(String mensagem);
}
