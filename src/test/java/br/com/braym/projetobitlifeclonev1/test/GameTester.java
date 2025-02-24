package br.com.braym.projetobitlifeclonev1.test;

import br.com.braym.projetobitlifeclonev1.application.EventManager;
import br.com.braym.projetobitlifeclonev1.application.GameEngine;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.impl.ConsoleObservador;
import java.util.logging.Logger;

/**
 * Classe de teste para iniciar o jogo.
 */
public class GameTester {
    private static final Logger LOGGER = Logger.getLogger(GameTester.class.getName());

    public static void main(String[] args) {
        // Cria o personagem e adiciona um observador para monitoramento
        Personagem personagem = new Personagem("Braym");
        personagem.adicionarObservador(new ConsoleObservador());
        System.out.println("Personagem criado: " + personagem.getNome());
        System.out.println("Idade inicial: " + personagem.getIdade());
        
        // Inicializa o EventManager com o arquivo de eventos
        EventManager eventManager = new EventManager("eventos.json");
        
        // Inicia o GameEngine
        GameEngine gameEngine = new GameEngine(personagem, eventManager);
        gameEngine.run();
    }
}
