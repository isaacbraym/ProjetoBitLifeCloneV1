package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;

/**
 * Representa o estado Adulto do personagem.
 */
public class Adulto implements EstadoVida {

    @Override
    public EstadoVida proximoEstado(Personagem personagem) {
        if (personagem.getIdade() >= 65) {
            System.out.println("Transição: Adulto para Velhice");
            return new Velhice();
        }
        return this;
    }

    @Override
    public String getEstado() {
        return "Adulto";
    }
}
