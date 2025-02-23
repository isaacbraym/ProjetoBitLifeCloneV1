package br.com.braym.projetobitlifeclonev1.interfaces;

import br.com.braym.projetobitlifeclonev1.Personagem;

/**
 * Interface que define o comportamento dos estados de vida do personagem.
 */
public interface EstadoVida {
    /**
     * Realiza a transição para o próximo estado com base no atributo do personagem.
     * @param personagem o personagem que terá seu estado avaliado
     */
    void proximoEstado(Personagem personagem);

    /**
     * Retorna uma representação textual do estado.
     * @return o nome do estado
     */
    String getEstado();
}
