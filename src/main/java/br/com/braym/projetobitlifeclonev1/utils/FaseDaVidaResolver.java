package br.com.braym.projetobitlifeclonev1.utils;

public class FaseDaVidaResolver {
    public static String getFaseDaVidaFolder(int idade) {
        if (idade < 3) {
            return "01-PrimeiraInfancia_0-3";
        } else if (idade < 6) {
            return "02-SegundaInfancia_3-6";
        } else if (idade < 11) {
            return "03-TerceiraInfancia_6-10";
        } else if (idade < 15) {
            return "04-AdolescenciaInicial_11-14";
        } else if (idade < 18) {
            return "05-AdolescenciaMedia_15-17";
        } else if (idade < 22) {
            return "06-AdolescenciaTardia_18-21";
        } else if (idade < 30) {
            return "07-Juventude_22-29";
        } else if (idade < 40) {
            return "08-AdultoJovem_30-39";
        } else if (idade < 60) {
            return "09-MeiaIdade_40-59";
        } else if (idade < 75) {
            return "10-IdosoJovem_60-74";
        } else if (idade < 90) {
            return "11-IdosoMaduro_75-89";
        } else {
            return "12-VelhiceAvancada_90";
        }
    }
}
