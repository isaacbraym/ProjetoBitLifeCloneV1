package br.com.braym.projetobitlifeclonev1.utils;

import java.util.Random;

/**
 * Utilitário para geração de números aleatórios.
 */
public class RandomUtils {
    private static final Random RANDOM = new Random();

    /**
     * Gera um número aleatório entre min e max (inclusive).
     * @param min valor mínimo
     * @param max valor máximo
     * @return número aleatório entre min e max
     */
    public static int gerarNumero(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }
}
