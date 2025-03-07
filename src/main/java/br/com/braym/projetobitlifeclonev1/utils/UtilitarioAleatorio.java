package br.com.braym.projetobitlifeclonev1.utils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilitário unificado para geração de números e elementos aleatórios.
 * Substitui as classes RandomUtils e UtilitarioAleatorio.
 */
public final class UtilitarioAleatorio {
    // ThreadLocalRandom é mais eficiente para uso em múltiplas threads
    private static final Random RANDOM = ThreadLocalRandom.current();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Construtor privado para evitar instanciação
    private UtilitarioAleatorio() {
        throw new AssertionError("Esta classe não deve ser instanciada");
    }

    /**
     * Gera um número aleatório entre min e max (inclusive)
     * @param min Valor mínimo
     * @param max Valor máximo
     * @return Número aleatório entre min e max
     */
    public static int gerarNumero(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("O valor mínimo não pode ser maior que o máximo");
        }
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * Gera um número decimal aleatório entre min e max
     * @param min Valor mínimo
     * @param max Valor máximo
     * @return Número decimal aleatório entre min e max
     */
    public static double gerarNumeroDecimal(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("O valor mínimo não pode ser maior que o máximo");
        }
        return min + (max - min) * RANDOM.nextDouble();
    }

    /**
     * Retorna um elemento aleatório de uma lista
     * @param <T> Tipo dos elementos da lista
     * @param lista Lista de elementos
     * @return Elemento aleatório ou null se a lista estiver vazia
     */
    public static <T> T elementoAleatorio(List<T> lista) {
        if (lista == null || lista.isEmpty()) {
            return null;
        }
        return lista.get(RANDOM.nextInt(lista.size()));
    }

    /**
     * Retorna um elemento aleatório de uma coleção
     * @param <T> Tipo dos elementos da coleção
     * @param colecao Coleção de elementos
     * @return Elemento aleatório ou null se a coleção estiver vazia
     */
    public static <T> T elementoAleatorio(Collection<T> colecao) {
        if (colecao == null || colecao.isEmpty()) {
            return null;
        }

        int indice = RANDOM.nextInt(colecao.size());
        int i = 0;
        for (T elemento : colecao) {
            if (i == indice) {
                return elemento;
            }
            i++;
        }

        return null; // Nunca deve chegar aqui
    }

    /**
     * Verifica se um evento aleatório acontece, baseado em uma probabilidade
     * @param probabilidade Probabilidade do evento acontecer (0-100)
     * @return true se o evento acontece, false caso contrário
     */
    public static boolean eventoAcontece(int probabilidade) {
        if (probabilidade <= 0) {
            return false;
        }
        if (probabilidade >= 100) {
            return true;
        }
        return RANDOM.nextInt(100) < probabilidade;
    }

    /**
     * Gera um número aleatório mais seguro para uso em operações sensíveis
     * @param min Valor mínimo
     * @param max Valor máximo
     * @return Número aleatório seguro entre min e max
     */
    public static int gerarNumeroSeguro(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("O valor mínimo não pode ser maior que o máximo");
        }
        return SECURE_RANDOM.nextInt(max - min + 1) + min;
    }
}