# Sistema de Eventos Comunitarios - CEFET-RJ

Sistema em Java para gerenciamento de eventos comunitarios
(palestras, oficinas, feiras), desenvolvido como atividade P2
da disciplina de Programacao Orientada a Objetos.

## Funcionalidades

- **CRUD de Usuarios**: cadastro, edicao e exclusao de organizadores,
  voluntarios e publico.
- **CRUD de Eventos**: cadastro, edicao e exclusao com titulo,
  descricao, data/hora, local, capacidade e categoria.
- **Inscricoes**: inscricao de participantes, cancelamento e
  registro de presenca.
- **Relatorios**: visao consolidada de cada evento com totais
  de inscritos e presentes.

## Regras de Negocio Implementadas

1. **Verificacao de lotacao**: impede inscricao quando o evento
   atingiu sua capacidade maxima.
2. **Conflito de horario de local**: impede dois eventos no mesmo
   local na mesma data/hora.
3. **Inscricao duplicada**: impede que o mesmo usuario se inscreva
   mais de uma vez no mesmo evento.

## Arquitetura

O projeto segue arquitetura em camadas:

- **Model**: classes de dados (Usuario, Evento, Inscricao, Participacao)
- **DAO**: acesso a dados com JDBC puro e PreparedStatement
- **Business**: regras de negocio e excecao customizada (NegocioException)
- **View**: interface grafica com Swing (JFrame, JTable, JComboBox, JOptionPane)

## Pre-requisitos

- JDK 17 ou superior (desenvolvido com JDK 21)
- MySQL 8.0 ou superior
- MySQL Connector/J 9.x (arquivo .jar na pasta lib/)

## Como Instalar e Executar

### 1. Criar o banco de dados

Abra o MySQL e execute o script:

```sql
mysql -u root -p < sql/criar_banco.sql
```

### 2. Compilar e executar o sistema

Você pode compilar e executar o projeto via terminal:

```bash
# Compilar
javac -cp "lib/mysql-connector-j-9.7.0.jar;src" -d bin src/Main.java

# Executar
java -cp "lib/mysql-connector-j-9.7.0.jar;bin" Main
```