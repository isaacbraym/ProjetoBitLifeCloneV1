package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.Personagem;
import br.com.braym.projetobitlifeclonev1.eventos.Evento;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import java.util.List;
import java.util.Scanner;

/**
 * Implementação concreta de um evento, permitindo execução interativa via console.
 */
public class EventoImpl extends Evento implements EventoInterface {

    public EventoImpl(String descricao, List<String> opcoes, List<Integer> efeitos, String atributo) {
        super(descricao, opcoes, efeitos, atributo);
    }
    
    @Override
    public void executarEvento(Personagem personagem) {
        System.out.println("Evento: " + getDescricao());
        List<String> opcoes = getOpcoes();
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i + 1) + ": " + opcoes.get(i));
        }
        System.out.print("Escolha uma opção: ");
        Scanner scanner = new Scanner(System.in);
        int escolha = scanner.nextInt() - 1; // Ajusta para índice (0-based)
        if (escolha >= 0 && escolha < opcoes.size()) {
            System.out.println("Você escolheu: " + opcoes.get(escolha));
            aplicarEfeito(personagem, escolha);
        } else {
            System.out.println("Opção inválida, nenhuma ação realizada.");
        }
    }
    
    /**
     * Aplica o efeito ao personagem com base na opção escolhida.
     * @param personagem o personagem a ser afetado
     * @param escolha o índice da opção escolhida
     */
    public void aplicarEfeito(Personagem personagem, int escolha) {
        int efeito = getEfeitos().get(escolha);
        String atributo = getAtributo().toLowerCase();
        
        switch (atributo) {
            case "financas":
                personagem.alterarFinancas(efeito);
                break;
            case "saude":
                personagem.setSaude(personagem.getSaude() + efeito);
                break;
            case "inteligencia":
                personagem.alterarInteligencia(efeito);
                break;
            case "felicidade":
                personagem.setFelicidade(personagem.getFelicidade() + efeito);
                break;
            // Adicionar outros casos conforme os atributos implementados
            default:
                System.out.println("Atributo não reconhecido: " + atributo);
                break;
        }
    }
}
