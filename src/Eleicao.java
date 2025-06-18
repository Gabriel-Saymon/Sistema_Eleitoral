import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Eleicao {
    private Map<Integer, Candidato> candidatos;
    private LocalDate dataEleicao;
    private List<Partido> partidos;
    private String codMunicipio;
    private int qtdVagas;


    public Eleicao(LocalDate dataEleicao, String codMunicipio) {
        this.dataEleicao = dataEleicao;
        this.codMunicipio = codMunicipio;
        this.candidatos = new HashMap<Integer, Candidato>();
        this.partidos = new LinkedList<Partido>();
        this.qtdVagas = 0;
    }

    public LinkedList<Candidato> getCandidatos(){
        return new LinkedList<Candidato>(candidatos.values());
    }

    /*
     * Adiciona candidato a eleição
     * <li>se for eleito, incrementa numero de vagas
     * @param pessoa a ser adicionada
     */
    /* 
    public void adicionaCandidatos(Candidato pessoa) {
        candidatos.put(pessoa.getNumeroUrna(), pessoa);
        if (pessoa.ehEleito()) {
            this.qtdVagas++;
        }
    }*/

    public LocalDate getDataEleicao(){
        return dataEleicao;
    }

     public LinkedList<Partido> getPartidos() {
        return new LinkedList<Partido>(partidos);

    }

    public void adicionaPartidos(Partido grupo) {
        partidos.add(grupo);
    }

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public int getQtdVagas() {
        return qtdVagas;
    }
    
}
