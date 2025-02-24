package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;

/**
 * Representa o estado de Adolescência do personagem.
 */
public class Adolescencia implements EstadoVida {

    @Override
    public EstadoVida proximoEstado(Personagem personagem) {
        if (personagem.getIdade() >= 18) {
            System.out.println("Transição: Adolescência para Adulto");
            return new Adulto();
        }
        return this;
    }

    @Override
    public String getEstado() {
        return "Adolescência";
    }
}
