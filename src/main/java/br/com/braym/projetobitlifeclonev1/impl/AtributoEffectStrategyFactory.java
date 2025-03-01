package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.domain.Atributo;

public class AtributoEffectStrategyFactory {

    public static AtributoEffectStrategy getStrategy(Atributo atributo) {
        switch (atributo) {
            case FINANCAS:
                return new FinancasEffect();
            case SAUDE:
                return new SaudeEffect();
            case FELICIDADE:
                return new FelicidadeEffect();
            case INTELIGENCIA:
                return new InteligenciaEffect();
            case SANIDADE:
                return new SanidadeEffect();
            case CARISMA:
                return new CarismaEffect();
            default:
                throw new IllegalArgumentException("Atributo não reconhecido: " + atributo);
        }
    }
}
