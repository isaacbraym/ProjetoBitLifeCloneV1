package br.com.braym.projetobitlifeclonev1.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Representa um evento no jogo, contendo descrição, opções, efeitos simples e
 * múltiplos.
 */
public class Evento {
	private static final Logger LOGGER = Logger.getLogger(Evento.class.getName());

	private String id;
	private String descricao;
	private List<String> opcoes;
	private List<Integer> efeitos; // Efeitos correspondentes às opções
	private String atributo;
	// Novo campo para suportar múltiplos efeitos (atributo -> valor)
	private Map<String, Integer> efeitosMultiplos;

	/**
	 * Construtor completo para Evento
	 * 
	 * @param id        Identificador único do evento
	 * @param descricao Descrição textual do evento
	 * @param opcoes    Lista de opções disponíveis para o jogador
	 * @param efeitos   Lista de valores correspondentes a cada opção
	 * @param atributo  Nome do atributo a ser afetado
	 */
	public Evento(String id, String descricao, List<String> opcoes, List<Integer> efeitos, String atributo) {
		this.id = id;
		this.descricao = descricao;
		this.opcoes = opcoes;
		this.efeitos = efeitos;
		this.atributo = atributo;
	}

	/**
	 * Construtor padrão necessário para deserialização JSON
	 */
	public Evento() {
	}

	// Getters e Setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<String> getOpcoes() {
		return opcoes;
	}

	public void setOpcoes(List<String> opcoes) {
		this.opcoes = opcoes;
	}

	public List<Integer> getEfeitos() {
		return efeitos;
	}

	public void setEfeitos(List<Integer> efeitos) {
		this.efeitos = efeitos;
	}

	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}

	public Map<String, Integer> getEfeitosMultiplos() {
		return efeitosMultiplos;
	}

	public void setEfeitosMultiplos(Map<String, Integer> efeitosMultiplos) {
		this.efeitosMultiplos = efeitosMultiplos;
	}

	/**
	 * Valida se o evento possui todos os dados necessários
	 * 
	 * @return true se o evento for válido, false caso contrário
	 */
	public boolean isValido() {
		boolean temOpcoes = opcoes != null && !opcoes.isEmpty();
		boolean temEfeitos = (efeitos != null && !efeitos.isEmpty())
				|| (efeitosMultiplos != null && !efeitosMultiplos.isEmpty());
		boolean temAtributoSeTemEfeito = !temEfeitos || (atributo != null && !atributo.isBlank());

		return id != null && !id.isBlank() && descricao != null && !descricao.isBlank() && temOpcoes && temEfeitos
				&& temAtributoSeTemEfeito;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Evento evento = (Evento) o;
		return Objects.equals(id, evento.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Evento{" + "id='" + id + '\'' + ", descricao='" + descricao + '\'' + ", opcoes="
				+ (opcoes != null ? opcoes.size() : 0) + " opções" + '}';
	}
}