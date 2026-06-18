package model;

public class Participacao {

    private int id;
    private int idInscricao;
    private boolean presente;
    private String observacao;

    public Participacao() {}

    public Participacao(int idInscricao, boolean presente, String observacao) {
        this.idInscricao = idInscricao;
        this.presente = presente;
        this.observacao = observacao;
    }

    public int getId()                                  { return id; }
    public void setId(int id)                           { this.id = id; }

    public int getIdInscricao()                         { return idInscricao; }
    public void setIdInscricao(int idInscricao)         { this.idInscricao = idInscricao; }

    public boolean isPresente()                         { return presente; }
    public void setPresente(boolean presente)           { this.presente = presente; }

    public String getObservacao()                       { return observacao; }
    public void setObservacao(String observacao)        { this.observacao = observacao; }
}
