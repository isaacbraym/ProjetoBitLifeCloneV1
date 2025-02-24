package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;

/**
 * Representa o estado de Infância do personagem.
 */
public class Infancia implements EstadoVida {

    @Override
    public EstadoVida proximoEstado(Personagem personagem) {
        if (personagem.getIdade() >= 12) {
            System.out.println("Transição: Infância para Adolescência");
            return new Adolescencia();
        }
        return this;
    }

    @Override
    public String getEstado() {
        return "Infância";
    }
}
