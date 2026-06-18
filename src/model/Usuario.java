package model;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String tipo;
    private String telefone;

    public Usuario() {}

    public Usuario(String nome, String email, String tipo, String telefone) {
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.telefone = telefone;
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public String getNome()              { return nome; }
    public void setNome(String nome)     { this.nome = nome; }

    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }

    public String getTipo()              { return tipo; }
    public void setTipo(String tipo)     { this.tipo = tipo; }

    public String getTelefone()               { return telefone; }
    public void setTelefone(String telefone)  { this.telefone = telefone; }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + tipo + ")";
    }
}