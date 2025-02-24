package br.com.braym.projetobitlifeclonev1.domain;

/**
 * Representa uma consequência derivada de uma decisão ou evento.
 */
public class Consequencia {
    private String opcao;
    private String atributo;
    private int valor;

    public String getOpcao() {
        return opcao;
    }

    public String getAtributo() {
        return atributo;
    }

    public int getValor() {
        return valor;
    }
}
