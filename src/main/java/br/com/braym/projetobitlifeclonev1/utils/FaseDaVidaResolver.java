package br.com.braym.projetobitlifeclonev1.utils;

/**
 * Utilitário para determinação da fase da vida com base na idade.
 * Utiliza recursos do Java 17 como switch expressions.
 */
public class FaseDaVidaResolver {

    /**
     * Obtém o nome da pasta que contém os eventos para a idade específica
     * @param idade Idade do personagem
     * @return Nome da pasta correspondente à fase da vida
     */
    public static String getPastaDaFaseDaVida(int idade) {
        return switch (idade) {
            case 0, 1, 2 -> "01-PrimeiraInfancia_0-3";
            case 3, 4, 5 -> "02-SegundaInfancia_3-6";
            case 6, 7, 8, 9, 10 -> "03-TerceiraInfancia_6-10";
            case 11, 12, 13, 14 -> "04-AdolescenciaInicial_11-14";
            case 15, 16, 17 -> "05-AdolescenciaMedia_15-17";
            case 18, 19, 20, 21 -> "06-AdolescenciaTardia_18-21";
            case 22, 23, 24, 25, 26, 27, 28, 29 -> "07-Juventude_22-29";
            case 30, 31, 32, 33, 34, 35, 36, 37, 38, 39 -> "08-AdultoJovem_30-39";
            default -> {
                if (idade < 60) yield "09-MeiaIdade_40-59";
                else if (idade < 75) yield "10-IdosoJovem_60-74";
                else if (idade < 90) yield "11-IdosoMaduro_75-89";
                else yield "12-VelhiceAvancada_90";
            }
        };
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
     * Obtém o enum de fase da vida correspondente à idade
     * @param idade Idade do personagem
     * @return Enum FaseDaVida correspondente
     */
    public static FaseDaVida obterFaseDaVida(int idade) {
        return switch (idade) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 -> FaseDaVida.INFANCIA;
            case 12, 13, 14, 15, 16, 17 -> FaseDaVida.ADOLESCENCIA;
            case 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 
                 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 
                 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64 -> FaseDaVida.ADULTO;
            default -> FaseDaVida.VELHICE;
        };
    }
    
    /**
     * Verifica se o personagem mudou de fase com a última alteração de idade
     * @param idadeAtual Idade atual do personagem
     * @param idadeAnterior Idade anterior do personagem
     * @return true se houve mudança de fase, false caso contrário
     */
    public static boolean houvelMudancaDeFase(int idadeAtual, int idadeAnterior) {
        return obterFaseDaVida(idadeAtual) != obterFaseDaVida(idadeAnterior);
    }
}