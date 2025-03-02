package br.com.braym.projetobitlifeclonev1.impl;

import br.com.braym.projetobitlifeclonev1.interfaces.Observador;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Implementação do padrão Observer para exibir notificações no console.
 */
public class ConsoleObservador implements Observador {
	private static final Logger LOGGER = Logger.getLogger(ConsoleObservador.class.getName());
	private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final boolean exibirTimestamp;
	private final boolean registrarLog;

	/**
	 * Construtor padrão
	 */
	public ConsoleObservador() {
		this(true, true);
	}

	/**
	 * Construtor com opções de configuração
	 * 
	 * @param exibirTimestamp Se true, exibe o timestamp junto com a mensagem
	 * @param registrarLog    Se true, registra as mensagens no log
	 */
	public ConsoleObservador(boolean exibirTimestamp, boolean registrarLog) {
		this.exibirTimestamp = exibirTimestamp;
		this.registrarLog = registrarLog;
	}

	@Override
	public void atualizar(String mensagem) {
		String mensagemFormatada = formatarMensagem(mensagem);
		System.out.println(mensagemFormatada);

		if (registrarLog) {
			LOGGER.info(mensagem);
		}
	}

	@Override
	public void atualizar(String mensagem, Object fonte) {
		String mensagemCompleta = mensagem + " [Fonte: " + fonte.getClass().getSimpleName() + "]";
		atualizar(mensagemCompleta);
	}

	/**
	 * Formata a mensagem adicionando timestamp se configurado
	 * 
	 * @param mensagem Mensagem original
	 * @return Mensagem formatada
	 */
	private String formatarMensagem(String mensagem) {
		if (exibirTimestamp) {
			String timestamp = LocalDateTime.now().format(FORMATADOR_DATA);
			return String.format("[%s] Notificação: %s", timestamp, mensagem);
		} else {
			return "Notificação: " + mensagem;
		}
	}
}