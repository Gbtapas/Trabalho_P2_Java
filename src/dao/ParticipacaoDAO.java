package dao;

import model.Participacao;
import java.sql.*;

public class ParticipacaoDAO {

    public void registrarPresenca(int idInscricao, boolean presente, String observacao) throws SQLException {
        Participacao existente = buscarPorInscricao(idInscricao);
        if (existente != null) {
            String sql = "UPDATE participacoes SET presente = ?, observacao = ? WHERE id_inscricao = ?";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, presente);
                ps.setString(2, observacao);
                ps.setInt(3, idInscricao);
                ps.executeUpdate();
            }
        } else {
            String sql = "INSERT INTO participacoes (id_inscricao, presente, observacao) VALUES (?,?,?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idInscricao);
                ps.setBoolean(2, presente);
                ps.setString(3, observacao);
                ps.executeUpdate();
            }
        }
    }


    public Participacao buscarPorInscricao(int idInscricao) throws SQLException {
        String sql = "SELECT * FROM participacoes WHERE id_inscricao = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idInscricao);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Participacao p = new Participacao();
                p.setId(rs.getInt("id"));
                p.setIdInscricao(rs.getInt("id_inscricao"));
                p.setPresente(rs.getBoolean("presente"));
                p.setObservacao(rs.getString("observacao"));
                return p;
            }
        }
        return null;
    }
}