package model;

import java.time.LocalDateTime;

public class Inscricao {

    private int id;
    private int idUsuario;
    private int idEvento;
    private LocalDateTime dataInscricao;

    public Inscricao() {}

    public Inscricao(int idUsuario, int idEvento) {
        this.idUsuario = idUsuario;
        this.idEvento = idEvento;
        this.dataInscricao = LocalDateTime.now();
    }

    public int getId()                                  { return id; }
    public void setId(int id)                           { this.id = id; }

    public int getIdUsuario()                           { return idUsuario; }
    public void setIdUsuario(int idUsuario)             { this.idUsuario = idUsuario; }

    public int getIdEvento()                            { return idEvento; }
    public void setIdEvento(int idEvento)               { this.idEvento = idEvento; }

    public LocalDateTime getDataInscricao()             { return dataInscricao; }
    public void setDataInscricao(LocalDateTime data)   { this.dataInscricao = data; }
}
