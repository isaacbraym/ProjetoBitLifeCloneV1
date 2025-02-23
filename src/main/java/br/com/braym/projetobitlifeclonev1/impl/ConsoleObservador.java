package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.interfaces.Observador;

/**
 * Implementação do padrão Observer para exibir notificações no console.
 */
public class ConsoleObservador implements Observador {

    @Override
    public void atualizar(String mensagem) {
        System.out.println("Notificação: " + mensagem);
    }
}
