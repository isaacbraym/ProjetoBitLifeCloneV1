package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;

public class FaseManager {

	private final EventoLoader eventoLoader;

	public FaseManager(EventoLoader eventoLoader) {
		this.eventoLoader = eventoLoader;
	}

	public void verificarMudancaFase(Personagem personagem, int idadeAnterior) {
		String faseFolderAnterior = FaseDaVidaResolver.getFaseDaVidaFolder(idadeAnterior);
		String faseFolderAtual = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());

		if (!faseFolderAnterior.equals(faseFolderAtual)) {
			eventoLoader.carregarEventosSeNecessario(faseFolderAtual);
		}
	}
}
