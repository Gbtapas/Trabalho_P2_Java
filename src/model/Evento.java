package model;

import java.time.LocalDateTime;

public class Evento {

    private int id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataHora;
    private String localEvento;
    private int capacidade;
    private String categoria;
    private int idOrganizador;

    public Evento() {}

    public Evento(String titulo, String descricao, LocalDateTime dataHora,
                  String localEvento, int capacidade, String categoria, int idOrganizador) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.localEvento = localEvento;
        this.capacidade = capacidade;
        this.categoria = categoria;
        this.idOrganizador = idOrganizador;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getTitulo()                   { return titulo; }
    public void setTitulo(String titulo)        { this.titulo = titulo; }

    public String getDescricao()                { return descricao; }
    public void setDescricao(String descricao)  { this.descricao = descricao; }

    public LocalDateTime getDataHora()                  { return dataHora; }
    public void setDataHora(LocalDateTime dataHora)     { this.dataHora = dataHora; }

    public String getLocalEvento()                      { return localEvento; }
    public void setLocalEvento(String localEvento)      { this.localEvento = localEvento; }

    public int getCapacidade()                  { return capacidade; }
    public void setCapacidade(int capacidade)   { this.capacidade = capacidade; }

    public String getCategoria()                { return categoria; }
    public void setCategoria(String categoria)  { this.categoria = categoria; }

    public int getIdOrganizador()                       { return idOrganizador; }
    public void setIdOrganizador(int idOrganizador)     { this.idOrganizador = idOrganizador; }

    @Override
    public String toString() {
        return id + " - " + titulo + " (" + categoria + ")";
    }
}