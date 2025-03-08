package br.com.braym.projetobitlifeclonev1.presentation;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.domain.Pessoa;
import br.com.braym.projetobitlifeclonev1.domain.Relacionamento;
import br.com.braym.projetobitlifeclonev1.domain.TipoRelacionamento;
import br.com.braym.projetobitlifeclonev1.service.ProvedorEntrada;

/**
 * Interface de usuário baseada em console para interação com o jogo.
 * Responsável por exibir informações e coletar entradas do usuário.
 */
public class InterfaceConsole {
	private static final String SEPARADOR = "=".repeat(30);
	private static final String TITULO_JOGO = "BitLife Clone";

	private final ProvedorEntrada provedorEntrada;

	/**
	 * Construtor com injeção de dependência
	 * 
	 * @param provedorEntrada Provedor de entrada do usuário
	 */
	public InterfaceConsole(ProvedorEntrada provedorEntrada) {
		this.provedorEntrada = provedorEntrada;
	}

	/**
	 * Exibe o menu principal
	 */
	public void mostrarMenu() {
	    System.out.println("\n" + SEPARADOR);
	    System.out.println(TITULO_JOGO);
	    System.out.println(SEPARADOR);
	    System.out.println("1. Envelhecer personagem");
	    System.out.println("2. Processar evento aleatório");
	    System.out.println("3. Mostrar status do personagem");
	    System.out.println("4. Salvar jogo");
	    System.out.println("5. Carregar jogo");
	    System.out.println("6. Relacionamentos");
	    System.out.println("7. Sair");
	    System.out.println(SEPARADOR);
	}

	/**
	 * Lê a opção escolhida pelo usuário
	 * 
	 * @return A opção escolhida
	 */
	public int lerOpcao() {
		return provedorEntrada.lerInteiroComIntervalo("Escolha uma opção: ", 1, 7);
	}

	/**
	 * Exibe o status atual do personagem
	 * 
	 * @param personagem O personagem cujos atributos serão exibidos
	 */
	public void mostrarStatus(Personagem personagem) {
		System.out.println("\n" + SEPARADOR);
		System.out.println("Status do Personagem");
		System.out.println(SEPARADOR);
		System.out.println("Nome: " + personagem.getNomeCompleto());
		System.out.println("Idade: " + personagem.getIdade());
		System.out.println("Estado de Vida: " + personagem.getEstadoVida().getEstado());

		System.out.println("\nAtributos:");
		exibirBarraProgresso("Saúde", personagem.getSaude());
		exibirBarraProgresso("Sanidade", personagem.getSanidade());
		exibirBarraProgresso("Felicidade", personagem.getFelicidade());
		exibirBarraProgresso("Inteligência", personagem.getInteligencia());
		exibirBarraProgresso("Carisma", personagem.getCarisma());
		exibirBarraProgresso("Aparência", personagem.getAparencia());
		System.out.println("Finanças: " + personagem.getFinancas());

		System.out.println(SEPARADOR);
	}

	/**
	 * Exibe uma barra de progresso para um atributo
	 * 
	 * @param nomeAtributo Nome do atributo
	 * @param valor        Valor do atributo (0-100)
	 */
	private void exibirBarraProgresso(String nomeAtributo, int valor) {
		final int tamanhoMaximo = 20;
		int barras = Math.round((float) valor / 100 * tamanhoMaximo);

		StringBuilder barra = new StringBuilder();
		barra.append("[");
		for (int i = 0; i < tamanhoMaximo; i++) {
			barra.append(i < barras ? "█" : " ");
		}
		barra.append("]");

		System.out.printf("%-12s %s %3d%%\n", nomeAtributo + ":", barra.toString(), valor);
	}

	/**
	 * Exibe uma mensagem de boas-vindas
	 * 
	 * @param personagem Personagem do jogo
	 */
	public void exibirBoasVindas(Personagem personagem) {
	    System.out.println("\n" + SEPARADOR);
	    System.out.println("  Bem-vindo ao " + TITULO_JOGO + "!");
	    System.out.println(SEPARADOR);
	    System.out.println("Você está jogando como: " + personagem.getNomeCompleto());
	    System.out.println("Idade inicial: " + personagem.getIdade());
	    System.out.println("Finanças iniciais: R$" + personagem.getFinancas() + ",00");
	    
	    // Exibir informações sobre os pais
	    Relacionamento relPai = personagem.getGerenciadorRelacionamentos().getRelacionamentoPorTipo(TipoRelacionamento.PAI);
	    Relacionamento relMae = personagem.getGerenciadorRelacionamentos().getRelacionamentoPorTipo(TipoRelacionamento.MAE);
	    
	    System.out.println("\nSua família:");
	    if (relPai != null) {
	        Pessoa pai = relPai.getPessoa();
	        String profissaoPai = pai.getProfissao() != null ? pai.getProfissao().getNome() : "Desempregado";
	        System.out.println("Pai: " + pai.getNomeCompleto() + ", " + pai.getIdade() + " anos");
	        System.out.println("     Profissão: " + profissaoPai + " (Salário: R$" + pai.getSalario() + ",00)");
	    }
	    
	    if (relMae != null) {
	        Pessoa mae = relMae.getPessoa();
	        String profissaoMae = mae.getProfissao() != null ? mae.getProfissao().getNome() : "Desempregada";
	        System.out.println("Mãe: " + mae.getNomeCompleto() + ", " + mae.getIdade() + " anos");
	        System.out.println("     Profissão: " + profissaoMae + " (Salário: R$" + mae.getSalario() + ",00)");
	    }
	    
	    System.out.println("\nViva sua vida virtual, tome decisões e veja como elas afetam seu futuro!");
	    System.out.println(SEPARADOR);

	    System.out.println("\nPressione ENTER para começar...");
	    provedorEntrada.lerLinha("");
	}

	/**
	 * Exibe uma mensagem informativa
	 * 
	 * @param mensagem Mensagem a ser exibida
	 */
	public void exibirMensagem(String mensagem) {
		System.out.println("\n" + mensagem);
	}
}