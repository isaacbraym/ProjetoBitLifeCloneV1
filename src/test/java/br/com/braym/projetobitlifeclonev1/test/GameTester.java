package br.com.braym.projetobitlifeclonev1.test;

import java.util.Arrays;
import java.util.List;

import br.com.braym.projetobitlifeclonev1.Personagem;
import br.com.braym.projetobitlifeclonev1.eventos.Evento;
import br.com.braym.projetobitlifeclonev1.impl.Adolescencia;
import br.com.braym.projetobitlifeclonev1.impl.Adulto;
import br.com.braym.projetobitlifeclonev1.impl.ConsoleObservador;
import br.com.braym.projetobitlifeclonev1.impl.EventoImpl;
import br.com.braym.projetobitlifeclonev1.impl.Infancia;
import br.com.braym.projetobitlifeclonev1.impl.Velhice;
import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;
import br.com.braym.projetobitlifeclonev1.utils.EventoJSONReader;
import br.com.braym.projetobitlifeclonev1.utils.RandomUtils;

/**
 * Classe de teste para simulação e validação das regras do jogo.
 */
public class GameTester {

    public static void main(String[] args) {
        // Cria o personagem e adiciona um observador para monitoramento
        Personagem personagem = new Personagem("Braym");
        personagem.adicionarObservador(new ConsoleObservador());
        System.out.println("Personagem criado: " + personagem.getNome());
        System.out.println("Idade inicial: " + personagem.getIdade());

        // Simula o envelhecimento
        personagem.envelhecer();
        System.out.println("Idade após envelhecer: " + personagem.getIdade());

        // Verifica o estado de vida atual (Exemplo: Infância)
        EstadoVida estado = new Infancia();
        System.out.println("Estado atual: " + estado.getEstado());
        estado.proximoEstado(personagem);

        // Cria e executa um evento manualmente
        EventoImpl eventoManual = new EventoImpl(
            "Você encontra um tesouro!", 
            Arrays.asList("Abrir o baú", "Ignorar"), 
            Arrays.asList(10, 0), 
            "financas"
        );
        eventoManual.executarEvento(personagem);

        // Leitura de eventos via JSON
        List<Evento> eventos = EventoJSONReader.lerEventos("eventos.json");
        if (!eventos.isEmpty()) {
            System.out.println("Eventos lidos do JSON:");
            // Seleciona aleatoriamente um evento da lista
            int indiceAleatorio = RandomUtils.gerarNumero(0, eventos.size() - 1);
            Evento eventoSelecionado = eventos.get(indiceAleatorio);
            System.out.println("Evento selecionado: " + eventoSelecionado.getDescricao());
            
            // Se o evento implementa a interface EventoInterface, executa-o com o personagem
            if (eventoSelecionado instanceof br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface) {
                ((br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface) eventoSelecionado).executarEvento(personagem);
            }
        } else {
            System.out.println("Nenhum evento encontrado no JSON.");
        }

        // Exemplos de transição de estados
        EstadoVida estadoAdolescencia = new Adolescencia();
        estadoAdolescencia.proximoEstado(personagem);

        EstadoVida estadoAdulto = new Adulto();
        estadoAdulto.proximoEstado(personagem);

        EstadoVida estadoVelhice = new Velhice();
        estadoVelhice.proximoEstado(personagem);
    }
}
