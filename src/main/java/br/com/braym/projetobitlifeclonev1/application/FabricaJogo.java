package br.com.braym.projetobitlifeclonev1.application;

import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.impl.ConsoleObservador;
import br.com.braym.projetobitlifeclonev1.infrastructure.persistence.GerenciadorSalvamentoJogo;
import br.com.braym.projetobitlifeclonev1.presentation.InterfaceConsole;
import br.com.braym.projetobitlifeclonev1.service.CarregadorEventos;
import br.com.braym.projetobitlifeclonev1.service.GerenciadorEventos;
import br.com.braym.projetobitlifeclonev1.service.ProvedorEntrada;

/**
 * Fábrica para criar instâncias do jogo e seus componentes.
 * Implementa o padrão Factory para facilitar a criação de objetos e suas dependências.
 */
public class FabricaJogo {
    
    private final String pastaBases;
    
    /**
     * Construtor que define a pasta base de arquivos
     * @param pastaBases Caminho da pasta base de arquivos
     */
    public FabricaJogo(String pastaBases) {
        this.pastaBases = pastaBases;
    }
    
    /**
     * Cria uma instância completa do motor do jogo com todas as dependências
     * @param nomePersonagem Nome do personagem inicial
     * @return Motor do jogo pronto para execução
     */
    public MotorJogo criarJogo(String nomePersonagem) {
        // Cria as dependências necessárias
        ProvedorEntrada provedorEntrada = criarProvedorEntrada();
        GerenciadorSalvamentoJogo gerenciadorSalvamento = criarGerenciadorSalvamento();
        CarregadorEventos carregadorEventos = criarCarregadorEventos();
        GerenciadorEventos gerenciadorEventos = criarGerenciadorEventos(provedorEntrada);        InterfaceConsole interfaceConsole = criarInterfaceConsole(provedorEntrada);
        
        // Cria o personagem
        Personagem personagem = criarPersonagem(nomePersonagem);
        
        // Cria e retorna o motor do jogo
        return new MotorJogo(
            personagem,
            gerenciadorEventos,
            interfaceConsole,
            provedorEntrada,
            gerenciadorSalvamento
        );
    }
    
    /**
     * Cria uma instância do motor do jogo com um personagem existente
     * @param personagem Personagem carregado
     * @return Motor do jogo pronto para execução
     */
    public MotorJogo criarJogoComPersonagem(Personagem personagem) {
        if (personagem == null) {
            throw new IllegalArgumentException("Personagem não pode ser nulo");
        }
        
        // Cria as dependências necessárias
        ProvedorEntrada provedorEntrada = criarProvedorEntrada();
        GerenciadorSalvamentoJogo gerenciadorSalvamento = criarGerenciadorSalvamento();
        GerenciadorEventos gerenciadorEventos = criarGerenciadorEventos(provedorEntrada);
        InterfaceConsole interfaceConsole = criarInterfaceConsole(provedorEntrada);
        
        // Garante que o personagem tem um observador
        personagem.adicionarObservador(new ConsoleObservador());
        
        // Cria e retorna o motor do jogo
        return new MotorJogo(
                personagem,
                gerenciadorEventos,
                interfaceConsole,
                provedorEntrada,
                gerenciadorSalvamento
            );
    }
    
    /**
     * Cria o provedor de entrada do usuário
     * @return Provedor de entrada configurado
     */
    public ProvedorEntrada criarProvedorEntrada() {
        return new ProvedorEntrada();
    }
    
    /**
     * Cria o gerenciador de salvamento do jogo
     * @return Gerenciador de salvamento configurado
     */
    public GerenciadorSalvamentoJogo criarGerenciadorSalvamento() {
        return new GerenciadorSalvamentoJogo();
    }
    
    /**
     * Cria o carregador de eventos
     * @return Carregador de eventos configurado
     */
    public CarregadorEventos criarCarregadorEventos() {
        return new CarregadorEventos(pastaBases);
    }
    
    /**
     * Cria o gerenciador de eventos
     * @param carregadorEventos Carregador de eventos
     * @param provedorEntrada Provedor de entrada
     * @return Gerenciador de eventos configurado
     */
    public GerenciadorEventos criarGerenciadorEventos(ProvedorEntrada provedorEntrada) {
        return new GerenciadorEventos(pastaBases, provedorEntrada);
    }
    
    /**
     * Cria a interface de console
     * @param provedorEntrada Provedor de entrada
     * @return Interface de console configurada
     */
    public InterfaceConsole criarInterfaceConsole(ProvedorEntrada provedorEntrada) {
        return new InterfaceConsole(provedorEntrada);
    }
    
    /**
     * Cria um personagem com nome específico
     * @param nome Nome do personagem
     * @return Personagem configurado
     */
    public Personagem criarPersonagem(String nome) {
        Personagem personagem = new Personagem(nome);
        personagem.adicionarObservador(new ConsoleObservador());
        return personagem;
    }
}