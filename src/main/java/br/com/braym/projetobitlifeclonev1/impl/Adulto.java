package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;

/**
 * Representa o estado Adulto do personagem.
 */
public class Adulto implements EstadoVida {

    @Override
    public void proximoEstado(Personagem personagem) {
        if (personagem.getIdade() >= 65) {
            System.out.println("Transição: Adulto para Velhice");
            // Realize a transição para o estado Velhice, se aplicável
        }
    }

    @Override
    public String getEstado() {
        return "Adulto";
    }
}
