package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;

/**
 * Representa o estado de Velhice do personagem.
 */
public class Velhice implements EstadoVida {

    @Override
    public EstadoVida proximoEstado(Personagem personagem) {
        System.out.println("O personagem está na Velhice. Fim de transições.");
        return this;
    }

    @Override
    public String getEstado() {
        return "Velhice";
    }
}
