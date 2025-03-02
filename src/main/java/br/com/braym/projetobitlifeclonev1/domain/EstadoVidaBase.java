package br.com.braym.projetobitlifeclonev1.domain;

import br.com.braym.projetobitlifeclonev1.interfaces.EstadoVida;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;

/**
 * Implementação base para o padrão State que gerencia os estados de vida do
 * personagem. Contém implementações aninhadas para cada fase específica da
 * vida.
 */
public abstract class EstadoVidaBase implements EstadoVida {

	/**
	 * Determina o próximo estado com base na idade do personagem
	 * 
	 * @param personagem Personagem que terá seu estado atualizado
	 * @return O próximo estado apropriado
	 */
	@Override
	public EstadoVida proximoEstado(Personagem personagem) {
		return switch (FaseDaVidaResolver.obterFaseDaVida(personagem.getIdade())) {
		case INFANCIA -> (this instanceof Infancia) ? this : new Infancia();
		case ADOLESCENCIA -> (this instanceof Adolescencia) ? this : new Adolescencia();
		case ADULTO -> (this instanceof Adulto) ? this : new Adulto();
		case VELHICE -> (this instanceof Velhice) ? this : new Velhice();
		};
	}

	/**
	 * Estado de Infância (0-11 anos)
	 */
	public static class Infancia extends EstadoVidaBase {
		@Override
		public String getEstado() {
			return "Infância";
		}

		@Override
		public String getDescricao() {
			return "Período de desenvolvimento inicial, caracterizado por rápido crescimento físico e cognitivo.";
		}
	}

	/**
	 * Estado de Adolescência (12-17 anos)
	 */
	public static class Adolescencia extends EstadoVidaBase {
		@Override
		public String getEstado() {
			return "Adolescência";
		}

		@Override
		public String getDescricao() {
			return "Fase de transição entre a infância e a vida adulta, marcada por mudanças biológicas e psicológicas.";
		}
	}

	/**
	 * Estado de Adulto (18-64 anos)
	 */
	public static class Adulto extends EstadoVidaBase {
		@Override
		public String getEstado() {
			return "Adulto";
		}

		@Override
		public String getDescricao() {
			return "Período de maturidade, caracterizado por independência, responsabilidades e estabilidade.";
		}
	}

	/**
	 * Estado de Velhice (65+ anos)
	 */
	public static class Velhice extends EstadoVidaBase {
		@Override
		public String getEstado() {
			return "Velhice";
		}

		@Override
		public String getDescricao() {
			return "Fase final da vida, caracterizada por mudanças físicas e sabedoria acumulada.";
		}
	}
}