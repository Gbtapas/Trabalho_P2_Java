CREATE DATABASE IF NOT EXISTS trabalho_p2;
USE trabalho_p2;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    telefone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    data_hora DATETIME NOT NULL,
    local_evento VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    id_organizador INT NOT NULL,
    FOREIGN KEY (id_organizador) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS inscricoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_evento INT NOT NULL,
    data_inscricao DATETIME NOT NULL,
    UNIQUE (id_usuario, id_evento),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_evento) REFERENCES eventos(id)
);

CREATE TABLE IF NOT EXISTS participacoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_inscricao INT NOT NULL UNIQUE,
    presente TINYINT(1) NOT NULL DEFAULT 0,
    observacao VARCHAR(255),
    FOREIGN KEY (id_inscricao) REFERENCES inscricoes(id) ON DELETE CASCADE
);
