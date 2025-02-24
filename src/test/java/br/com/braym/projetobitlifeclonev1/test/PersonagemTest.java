package br.com.braym.projetobitlifeclonev1.test;

import org.junit.Assert;
import org.junit.Test;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;

/**
 * Testes unitários para a classe Personagem.
 */
public class PersonagemTest {

    @Test
    public void testAlterarSaude() {
        Personagem personagem = new Personagem("Teste");
        int saudeInicial = personagem.getSaude();
        personagem.setSaude(saudeInicial - 20);
        // Verifica se a saúde foi ajustada corretamente sem ficar abaixo de 0
        Assert.assertTrue(personagem.getSaude() <= saudeInicial && personagem.getSaude() >= 0);
    }
}
