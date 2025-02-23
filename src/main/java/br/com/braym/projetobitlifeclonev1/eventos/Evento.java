package br.com.braym.projetobitlifeclonev1.eventos;

import java.util.List;

/**
 * Representa um evento no jogo, contendo descrição, opções, efeitos e o
 * atributo afetado.
 */
public class Evento {
	private String descricao;
	private List<String> opcoes;
	private List<Integer> efeitos; // Efeitos correspondentes às opções
	private String atributo; // Atributo do Personagem a ser afetado

	/**
	 * Construtor utilizado para deserialização via JSON.
	 */
	public Evento(String descricao, List<String> opcoes, List<Integer> efeitos, String atributo) {
		this.descricao = descricao;
		this.opcoes = opcoes;
		this.efeitos = efeitos;
		this.atributo = atributo;
	}

	/**
	 * Construtor padrão necessário para bibliotecas de deserialização, como Gson.
	 */
	public Evento() {
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

	/**
	 * Aplica o efeito de uma escolha. Esta implementação genérica apenas exibe o
	 * efeito.
	 * 
	 * @param escolha o índice da opção escolhida
	 */
	public void aplicarEfeito(int escolha) {
		if (escolha >= 0 && escolha < efeitos.size()) {
			System.out.println("Aplicando efeito: " + efeitos.get(escolha));
		} else {
			System.out.println("Escolha inválida.");
		}
	}
}
