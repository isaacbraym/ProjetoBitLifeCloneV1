package br.com.braym.projetobitlifeclonev1.utils;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Utilitário para determinar a fase da vida e pasta correspondente com base na idade.
 * Implementado usando TreeMap para melhor escalabilidade e facilidade de manutenção.
 */
public class FaseDaVidaResolver {
    
    // Mapeamento de idade para fase da vida
    private static final NavigableMap<Integer, FaseDaVida> FASES_POR_IDADE = new TreeMap<>();
    
    // Mapeamento de idade para pasta
    private static final NavigableMap<Integer, String> PASTAS_POR_IDADE = new TreeMap<>();
    
    // Inicialização dos mapeamentos
    static {
        // Configuração das fases da vida
        FASES_POR_IDADE.put(0, FaseDaVida.INFANCIA);     // 0-11 anos
        FASES_POR_IDADE.put(12, FaseDaVida.ADOLESCENCIA); // 12-17 anos
        FASES_POR_IDADE.put(18, FaseDaVida.ADULTO);      // 18-64 anos
        FASES_POR_IDADE.put(65, FaseDaVida.VELHICE);     // 65+ anos
        
        // Configuração das pastas por idade
        PASTAS_POR_IDADE.put(0, "01-PrimeiraInfancia_0-3");
        PASTAS_POR_IDADE.put(3, "02-SegundaInfancia_3-6");
        PASTAS_POR_IDADE.put(6, "03-TerceiraInfancia_6-10");
        PASTAS_POR_IDADE.put(11, "04-AdolescenciaInicial_11-14");
        PASTAS_POR_IDADE.put(15, "05-AdolescenciaMedia_15-17");
        PASTAS_POR_IDADE.put(18, "06-AdolescenciaTardia_18-21");
        PASTAS_POR_IDADE.put(22, "07-Juventude_22-29");
        PASTAS_POR_IDADE.put(30, "08-AdultoJovem_30-39");
        PASTAS_POR_IDADE.put(40, "09-MeiaIdade_40-59");
        PASTAS_POR_IDADE.put(60, "10-IdosoJovem_60-74");
        PASTAS_POR_IDADE.put(75, "11-IdosoMaduro_75-89");
        PASTAS_POR_IDADE.put(90, "12-VelhiceAvancada_90");
    }
    
    /**
     * Obtém a pasta da fase da vida para a idade específica
     * @param idade Idade do personagem
     * @return Nome da pasta correspondente
     */
    public static String getPastaDaFaseDaVida(int idade) {
        return PASTAS_POR_IDADE.floorEntry(idade).getValue();
    }
    
    /**
     * Método para compatibilidade com código legado
     * @param idade Idade do personagem
     * @return Nome da pasta da fase da vida
     */
    public static String getFaseDaVidaFolder(int idade) {
        return getPastaDaFaseDaVida(idade);
    }
    
    /**
     * Obtém o enum da fase da vida correspondente à idade
     * @param idade Idade do personagem
     * @return Enum FaseDaVida correspondente
     */
    public static FaseDaVida obterFaseDaVida(int idade) {
        return FASES_POR_IDADE.floorEntry(idade).getValue();
    }
    
    /**
     * Verifica se houve mudança de fase entre duas idades
     * @param idadeAtual Idade atual
     * @param idadeAnterior Idade anterior
     * @return true se houve mudança de fase
     */
    public static boolean houveMudancaDeFase(int idadeAtual, int idadeAnterior) {
        return obterFaseDaVida(idadeAtual) != obterFaseDaVida(idadeAnterior);
    }
    
    /**
     * Verifica se houve mudança de pasta entre duas idades
     * @param idadeAtual Idade atual
     * @param idadeAnterior Idade anterior
     * @return true se houve mudança de pasta
     */
    public static boolean houveMudancaDePasta(int idadeAtual, int idadeAnterior) {
        return !getPastaDaFaseDaVida(idadeAtual).equals(getPastaDaFaseDaVida(idadeAnterior));
    }
    
    /**
     * Adiciona uma nova configuração de pasta para uma idade específica.
     * Útil para extensões ou testes.
     * @param idadeMinima Idade mínima para a pasta
     * @param nomePasta Nome da pasta
     */
    public static void adicionarConfiguracaoPasta(int idadeMinima, String nomePasta) {
        PASTAS_POR_IDADE.put(idadeMinima, nomePasta);
    }
    
    /**
     * Adiciona uma nova configuração de fase para uma idade específica.
     * Útil para extensões ou testes.
     * @param idadeMinima Idade mínima para a fase
     * @param fase Enum da fase
     */
    public static void adicionarConfiguracaoFase(int idadeMinima, FaseDaVida fase) {
        FASES_POR_IDADE.put(idadeMinima, fase);
    }
}