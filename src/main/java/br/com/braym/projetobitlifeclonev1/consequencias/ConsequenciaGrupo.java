package br.com.braym.projetobitlifeclonev1.consequencias;

import java.util.List;

/**
 * Agrupa uma coleção de consequências associadas a um determinado
 * identificador.
 */
public class ConsequenciaGrupo {
	private int id;
	private List<Consequencia> efeitos;

	public int getId() {
		return id;
	}

	public List<Consequencia> getEfeitos() {
		return efeitos;
	}
}
