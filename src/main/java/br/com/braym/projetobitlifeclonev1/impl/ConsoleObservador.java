package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.interfaces.Observador;
import java.util.logging.Logger;

/**
 * Implementação do padrão Observer para exibir notificações no console.
 */
public class ConsoleObservador implements Observador {
    private static final Logger LOGGER = Logger.getLogger(ConsoleObservador.class.getName());

    @Override
    public void atualizar(String mensagem) {
        System.out.println("Notificação: " + mensagem);
    }
}
