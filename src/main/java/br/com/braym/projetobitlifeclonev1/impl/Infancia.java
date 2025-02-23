package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;

/**
 * Representa o estado de Infância do personagem.
 */
public class Infancia implements EstadoVida {

    @Override
    public void proximoEstado(Personagem personagem) {
        if (personagem.getIdade() >= 12) {
            System.out.println("Transição: Infância para Adolescência");
            // Definir a transição de estado conforme a regra de negócio
        }
    }

    @Override
    public String getEstado() {
        return "Infância";
    }
}
