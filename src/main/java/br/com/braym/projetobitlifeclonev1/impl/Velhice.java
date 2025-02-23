package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;

/**
 * Representa o estado de Velhice do personagem.
 */
public class Velhice implements EstadoVida {

    @Override
    public void proximoEstado(Personagem personagem) {
        // Em Velhice, a transição pode não ocorrer ou indicar fim de jogo.
        System.out.println("O personagem está na Velhice. Mantenha o estado ou finalize o jogo.");
    }

    @Override
    public String getEstado() {
        return "Velhice";
    }
}
