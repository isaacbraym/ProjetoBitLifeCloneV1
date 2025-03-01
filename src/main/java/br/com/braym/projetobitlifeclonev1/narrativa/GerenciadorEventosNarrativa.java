package br.com.braym.projetobitlifeclonev1.narrativa;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import br.com.braym.projetobitlifeclonev1.domain.Personagem;
import br.com.braym.projetobitlifeclonev1.utils.FaseDaVidaResolver;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GerenciadorEventosNarrativa {
    private List<EventoNarrativa> eventos;
    private String baseFolder; // Ex: "Narrativas"

    /**
     * Construtor que carrega os eventos narrativos com base na fase da vida do personagem.
     */
    public GerenciadorEventosNarrativa(Personagem personagem, String baseFolder) {
        this.baseFolder = baseFolder;
        carregarEventos(personagem);
    }

    private void carregarEventos(Personagem personagem) {
        String faseFolder = FaseDaVidaResolver.getFaseDaVidaFolder(personagem.getIdade());
        String filePath = baseFolder + "/" + faseFolder + "/eventosNarrativa.json";
        eventos = carregarEventosDoArquivo(filePath);
    }

    private List<EventoNarrativa> carregarEventosDoArquivo(String nomeArquivo) {
        Gson gson = new Gson();
        InputStream is = GerenciadorEventosNarrativa.class.getResourceAsStream("/" + nomeArquivo);
        if (is == null) {
            System.err.println("Arquivo " + nomeArquivo + " não encontrado!");
            return null;
        }
        InputStreamReader reader = new InputStreamReader(is);
        Type listaTipo = new TypeToken<List<EventoNarrativa>>() {}.getType();
        return gson.fromJson(reader, listaTipo);
    }

    /**
     * Filtra os eventos de acordo com o período (para esta implementação, o arquivo já é específico à fase).
     */
    public List<EventoNarrativa> getEventos() {
        return eventos;
    }

    /**
     * Executa um evento narrativo para o personagem.
     */
    public void executarEventoNarrativa(Personagem personagem, EventoNarrativa evento) {
        if (evento == null) {
            System.out.println("Nenhum evento encontrado.");
            return;
        }
        System.out.println("\nEvento: " + evento.getDescricao());
        List<Escolha> opcoes = evento.getOpcoes();
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i + 1) + ": " + opcoes.get(i).getTexto());
        }
        System.out.print("Escolha uma opção: ");
        Scanner scanner = new Scanner(System.in);
        int escolhaIndex = scanner.nextInt() - 1;
        if (escolhaIndex < 0 || escolhaIndex >= opcoes.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        Escolha escolha = opcoes.get(escolhaIndex);
        System.out.println(escolha.getRetornoNarrativo());
        aplicarEfeitos(personagem, escolha.getEfeitos());
        String proximoEventoId = escolha.getIdProximoEvento();
        if (proximoEventoId != null && !proximoEventoId.isEmpty()) {
            EventoNarrativa proximoEvento = buscarEventoPorId(proximoEventoId);
            if (proximoEvento != null) {
                executarEventoNarrativa(personagem, proximoEvento);
            } else {
                System.out.println("Próximo evento não encontrado: " + proximoEventoId);
            }
        }
    }

    public EventoNarrativa buscarEventoPorId(String id) {
        return eventos.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public void aplicarEfeitos(Personagem personagem, java.util.Map<String, Integer> efeitos) {
        if (efeitos == null) {
            return;
        }
        efeitos.forEach((atributo, valor) -> {
            switch (atributo.toLowerCase()) {
                case "financas":
                    personagem.alterarFinancas(valor);
                    break;
                case "felicidade":
                    personagem.alterarFelicidade(valor);
                    break;
                case "sanidade":
                    personagem.alterarSanidade(valor);
                    break;
                case "saude":
                    personagem.setSaude(personagem.getSaude() + valor);
                    break;
                case "inteligencia":
                    personagem.alterarInteligencia(valor);
                    break;
                case "carisma":
                    personagem.setCarisma(personagem.getCarisma() + valor);
                    break;
                default:
                    System.out.println("Atributo desconhecido: " + atributo);
                    break;
            }
        });
    }

}
