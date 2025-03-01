package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;

public interface AtributoEffectStrategy {
	void aplicarEfeito(Personagem personagem, int valor);
}

class FinancasEffect implements AtributoEffectStrategy {
	@Override
	public void aplicarEfeito(Personagem personagem, int valor) {
		personagem.alterarFinancas(valor);
	}
}

class SaudeEffect implements AtributoEffectStrategy {
	@Override
	public void aplicarEfeito(Personagem personagem, int valor) {
		personagem.setSaude(personagem.getSaude() + valor);
	}
}

class FelicidadeEffect implements AtributoEffectStrategy {
	@Override
	public void aplicarEfeito(Personagem personagem, int valor) {
		personagem.alterarFelicidade(valor);
	}
}

class InteligenciaEffect implements AtributoEffectStrategy {
	@Override
	public void aplicarEfeito(Personagem personagem, int valor) {
		personagem.alterarInteligencia(valor);
	}
}

class SanidadeEffect implements AtributoEffectStrategy {
	@Override
	public void aplicarEfeito(Personagem personagem, int valor) {
		personagem.alterarSanidade(valor);
	}
}

class CarismaEffect implements AtributoEffectStrategy {
	@Override
	public void aplicarEfeito(Personagem personagem, int valor) {
		personagem.setCarisma(personagem.getCarisma() + valor);
	}
}