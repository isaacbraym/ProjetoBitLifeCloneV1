package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Evento;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.interfaces.EventoInterface;
import br.com.braym.projetobitlifeclonev1.service.EstrategiaEfeitoAtributo;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementação concreta de um evento que pode ser executado interativamente.
 */
public class EventoImpl implements EventoInterface {
	private static final Logger LOGGER = Logger.getLogger(EventoImpl.class.getName());

	private final String id;
	private final String descricao;
	private final List<String> opcoes;
	private final List<Integer> efeitos;
	private final String atributo;
	private final Map<String, Integer> efeitosMultiplos;
	private final Scanner scanner;

	/**
	 * Construtor completo
	 * 
	 * @param id               Identificador do evento
	 * @param descricao        Descrição do evento
	 * @param opcoes           Lista de opções para o jogador
	 * @param efeitos          Lista de valores correspondentes a cada opção
	 * @param atributo         Nome do atributo afetado
	 * @param efeitosMultiplos Mapa de múltiplos efeitos (atributo -> valor)
	 * @param scanner          Scanner para entrada do usuário
	 */
	public EventoImpl(String id, String descricao, List<String> opcoes, List<Integer> efeitos, String atributo,
			Map<String, Integer> efeitosMultiplos, Scanner scanner) {
		this.id = id;
		this.descricao = descricao;
		this.opcoes = opcoes;
		this.efeitos = efeitos;
		this.atributo = atributo;
		this.efeitosMultiplos = efeitosMultiplos;
		this.scanner = scanner != null ? scanner : new Scanner(System.in);
	}

	/**
	 * Construtor sem efeitosMultiplos
	 */
	public EventoImpl(String id, String descricao, List<String> opcoes, List<Integer> efeitos, String atributo,
			Scanner scanner) {
		this(id, descricao, opcoes, efeitos, atributo, null, scanner);
	}

	/**
	 * Construtor para compatibilidade com código legado Usa a descrição como ID,
	 * que é uma prática do código original
	 */
	public EventoImpl(String descricao, List<String> opcoes, List<Integer> efeitos, String atributo, Scanner scanner) {
		this(descricao, descricao, opcoes, efeitos, atributo, null, scanner);
	}

	/**
	 * Construtor que cria EventoImpl a partir de um objeto Evento
	 * 
	 * @param evento  Objeto Evento base
	 * @param scanner Scanner para entrada do usuário
	 */
	public EventoImpl(Evento evento, Scanner scanner) {
		this(evento.getId(), evento.getDescricao(), evento.getOpcoes(), evento.getEfeitos(), evento.getAtributo(),
				evento.getEfeitosMultiplos(), scanner);
	}

	@Override
	public void executarEvento(Personagem personagem) {
		System.out.println("Evento: " + descricao);
		for (int i = 0; i < opcoes.size(); i++) {
			System.out.println((i + 1) + ": " + opcoes.get(i));
		}

		int escolha = lerEscolhaValida();

		System.out.println("Você escolheu: " + opcoes.get(escolha));
		aplicarEfeito(personagem, escolha);
	}

	/**
	 * Lê e valida a escolha do usuário
	 * 
	 * @return Índice da escolha válida (0-based)
	 */
	private int lerEscolhaValida() {
		int escolha = -1;
		do {
			System.out.print("Escolha uma opção: ");
			try {
				escolha = Integer.parseInt(scanner.nextLine()) - 1;
			} catch (NumberFormatException e) {
				System.out.println("Por favor, digite um número válido.");
				continue;
			}

			if (escolha < 0 || escolha >= opcoes.size()) {
				System.out.println("Opção inválida. Tente novamente.");
				escolha = -1;
			}
		} while (escolha < 0);

		return escolha;
	}

	/**
	 * Aplica o efeito correspondente à escolha no personagem
	 * 
	 * @param personagem Personagem a ser afetado
	 * @param escolha    Índice da opção escolhida
	 */
	public void aplicarEfeito(Personagem personagem, int escolha) {
		try {
			if (efeitosMultiplos != null && !efeitosMultiplos.isEmpty()) {
				// Aplica múltiplos efeitos
				personagem.alterarAtributos(efeitosMultiplos);
				LOGGER.info("Aplicados múltiplos efeitos ao personagem: " + efeitosMultiplos);
			} else if (efeitos != null && escolha < efeitos.size()) {
				// Aplica efeito simples
				int efeito = efeitos.get(escolha);
				EstrategiaEfeitoAtributo estrategia = EstrategiaEfeitoAtributo.obterEstrategia(atributo);
				estrategia.aplicarEfeito(personagem, efeito);
				LOGGER.info("Aplicado efeito ao atributo " + atributo + ": " + efeito);
			} else {
				LOGGER.warning("Evento sem efeitos definidos ou escolha inválida.");
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Erro ao aplicar efeito: " + e.getMessage(), e);
			System.out.println("Não foi possível aplicar o efeito: " + e.getMessage());
		}
	}

	// Getters
	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public List<String> getOpcoes() {
		return opcoes;
	}

	public List<Integer> getEfeitos() {
		return efeitos;
	}

	public String getAtributo() {
		return atributo;
	}

	public Map<String, Integer> getEfeitosMultiplos() {
		return efeitosMultiplos;
	}
}