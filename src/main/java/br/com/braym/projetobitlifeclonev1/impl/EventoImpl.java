package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Implementação concreta de um evento, permitindo execução interativa via
 * console.
 */
public class EventoImpl extends Evento implements EventoInterface {
	private static final Logger LOGGER = Logger.getLogger(EventoImpl.class.getName());
	private Scanner scanner;

	/**
	 * Construtor atualizado para incluir o ID do evento
	 */
	public EventoImpl(String id, String descricao, List<String> opcoes, List<Integer> efeitos, String atributo) {
		super(id, descricao, opcoes, efeitos, atributo);
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Construtor sem ID - usa a própria descrição como ID (não recomendado)
	 */
	public EventoImpl(String descricao, List<String> opcoes, List<Integer> efeitos, String atributo) {
		super(descricao, descricao, opcoes, efeitos, atributo);
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Construtor com scanner fornecido
	 */
	public EventoImpl(String descricao, List<String> opcoes, List<Integer> efeitos, String atributo, Scanner scanner) {
		super(descricao, descricao, opcoes, efeitos, atributo);
		this.scanner = (scanner != null) ? scanner : new Scanner(System.in);
	}

	@Override
	public void executarEvento(Personagem personagem) {
		System.out.println("Evento: " + getDescricao());
		List<String> opcoes = getOpcoes();
		for (int i = 0; i < opcoes.size(); i++) {
			System.out.println((i + 1) + ": " + opcoes.get(i));
		}
		System.out.print("Escolha uma opção: ");
		int escolha = scanner.nextInt() - 1;
		scanner.nextLine(); // Consumir a quebra de linha restante

		if (escolha >= 0 && escolha < opcoes.size()) {
			System.out.println("Você escolheu: " + opcoes.get(escolha));
			aplicarEfeito(personagem, escolha);
		} else {
			System.out.println("Opção inválida, nenhuma ação realizada.");
		}
	}

	/**
	 * Aplica o efeito ao personagem com base na opção escolhida, considerando
	 * efeitos simples ou múltiplos.
	 * 
	 * @param personagem o personagem a ser afetado
	 * @param escolha    o índice da opção escolhida
	 */
	public void aplicarEfeito(Personagem personagem, int escolha) {
		if (getEfeitosMultiplos() != null && !getEfeitosMultiplos().isEmpty()) {
			for (Map.Entry<String, Integer> entry : getEfeitosMultiplos().entrySet()) {
				String atributo = entry.getKey().toLowerCase();
				int efeito = entry.getValue();
				aplicarEfeitoPorAtributo(personagem, atributo, efeito);
			}
		} else if (getEfeitos() != null && escolha < getEfeitos().size()) {
			int efeito = getEfeitos().get(escolha);
			String atributo = getAtributo().toLowerCase();
			aplicarEfeitoPorAtributo(personagem, atributo, efeito);
		} else {
			LOGGER.warning("Evento sem efeitos definidos ou escolha inválida.");
		}
	}

	private void aplicarEfeitoPorAtributo(Personagem personagem, String atributo, int efeito) {
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
		case "sanidade":
			personagem.alterarSanidade(efeito);
			break;
		case "carisma":
			personagem.setCarisma(personagem.getCarisma() + efeito);
			break;
		default:
			System.out.println("Atributo não reconhecido: " + atributo);
			break;
		}
	}
}