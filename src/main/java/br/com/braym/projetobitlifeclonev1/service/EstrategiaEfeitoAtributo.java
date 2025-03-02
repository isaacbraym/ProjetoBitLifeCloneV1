package br.com.braym.projetobitlifeclonev1.service;

import br.com.braym.projetobitlifeclonev1.domain.Atributo;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;

/**
 * Interface para o padrão Strategy que define como um efeito é aplicado a um
 * atributo do personagem. Implementa métodos estáticos para fácil obtenção de
 * estratégias.
 */
public interface EstrategiaEfeitoAtributo {

	/**
	 * Aplica o efeito do atributo ao personagem
	 * 
	 * @param personagem Personagem que receberá o efeito
	 * @param valor      Valor do efeito a ser aplicado
	 */
	void aplicarEfeito(Personagem personagem, int valor);

	/**
	 * Obtém uma estratégia de efeito a partir do nome do atributo
	 * 
	 * @param nomeAtributo Nome do atributo (insensível a caso)
	 * @return Estratégia para aplicação de efeito
	 * @throws IllegalArgumentException se o nome do atributo não for reconhecido
	 */
	static EstrategiaEfeitoAtributo obterEstrategia(String nomeAtributo) {
		try {
			Atributo atributo = Atributo.valueOf(nomeAtributo.toUpperCase());
			return obterEstrategia(atributo);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Atributo não reconhecido: " + nomeAtributo);
		}
	}

	/**
	 * Obtém uma estratégia de efeito a partir do enum Atributo
	 * 
	 * @param atributo Enum do atributo
	 * @return Estratégia para aplicação de efeito
	 */
	static EstrategiaEfeitoAtributo obterEstrategia(Atributo atributo) {
		return switch (atributo) {
		case FINANCAS -> (personagem, valor) -> personagem.alterarFinancas(valor);
		case SAUDE -> (personagem, valor) -> personagem.alterarSaude(valor);
		case FELICIDADE -> (personagem, valor) -> personagem.alterarFelicidade(valor);
		case INTELIGENCIA -> (personagem, valor) -> personagem.alterarInteligencia(valor);
		case SANIDADE -> (personagem, valor) -> personagem.alterarSanidade(valor);
		case CARISMA -> (personagem, valor) -> personagem.alterarCarisma(valor);
		};
	}
}