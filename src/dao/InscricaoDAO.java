package dao;

import model.Inscricao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscricaoDAO {

    public void inserir(Inscricao i) throws SQLException {
        String sql = "INSERT INTO inscricoes (id_usuario, id_evento, data_inscricao) VALUES (?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getIdUsuario());
            ps.setInt(2, i.getIdEvento());
            ps.setTimestamp(3, Timestamp.valueOf(i.getDataInscricao()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) i.setId(rs.getInt(1));
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM inscricoes WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contarInscricoes(int idEvento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscricoes WHERE id_evento = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public boolean existeInscricao(int idUsuario, int idEvento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscricoes WHERE id_usuario = ? AND id_evento = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEvento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public List<Object[]> listarDetalhesPorEvento(int idEvento) throws SQLException {
        String sql = "SELECT i.id, u.nome, u.tipo, i.data_inscricao, "
                + "COALESCE(p.presente, 0) AS presente "
                + "FROM inscricoes i "
                + "JOIN usuarios u ON i.id_usuario = u.id "
                + "LEFT JOIN participacoes p ON i.id = p.id_inscricao "
                + "WHERE i.id_evento = ? ORDER BY i.data_inscricao";
        List<Object[]> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nome");
                row[2] = rs.getString("tipo");
                row[3] = rs.getTimestamp("data_inscricao").toLocalDateTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                row[4] = rs.getBoolean("presente") ? "Sim" : "Nao";
                lista.add(row);
            }
        }
        return lista;
    }
}