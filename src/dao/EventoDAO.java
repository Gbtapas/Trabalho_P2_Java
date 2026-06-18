package dao;

import model.Evento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public void inserir(Evento e) throws SQLException {
        String sql = "INSERT INTO eventos (titulo, descricao, data_hora, local_evento, capacidade, categoria, id_organizador) "
                + "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDataHora()));
            ps.setString(4, e.getLocalEvento());
            ps.setInt(5, e.getCapacidade());
            ps.setString(6, e.getCategoria());
            ps.setInt(7, e.getIdOrganizador());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) e.setId(rs.getInt(1));
        }
    }

    public List<Evento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM eventos ORDER BY data_hora";
        List<Evento> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Evento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM eventos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public void atualizar(Evento e) throws SQLException {
        String sql = "UPDATE eventos SET titulo=?, descricao=?, data_hora=?, local_evento=?, "
                + "capacidade=?, categoria=?, id_organizador=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getDescricao());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDataHora()));
            ps.setString(4, e.getLocalEvento());
            ps.setInt(5, e.getCapacidade());
            ps.setString(6, e.getCategoria());
            ps.setInt(7, e.getIdOrganizador());
            ps.setInt(8, e.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM eventos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existeConflitoHorario(String local, LocalDateTime dataHora, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM eventos WHERE local_evento = ? AND data_hora = ? AND id != ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, local);
            ps.setTimestamp(2, Timestamp.valueOf(dataHora));
            ps.setInt(3, idExcluir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public List<Object[]> gerarRelatorio() throws SQLException {
        String sql = "SELECT e.titulo, e.categoria, e.data_hora, e.capacidade, "
                + "COUNT(i.id) AS total_inscritos, "
                + "COALESCE(SUM(CASE WHEN p.presente = 1 THEN 1 ELSE 0 END),0) AS total_presentes "
                + "FROM eventos e "
                + "LEFT JOIN inscricoes i ON e.id = i.id_evento "
                + "LEFT JOIN participacoes p ON i.id = p.id_inscricao "
                + "GROUP BY e.id ORDER BY e.data_hora";
        List<Object[]> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] linha = new Object[6];
                linha[0] = rs.getString("titulo");
                linha[1] = rs.getString("categoria");
                linha[2] = rs.getTimestamp("data_hora").toLocalDateTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                linha[3] = rs.getInt("capacidade");
                linha[4] = rs.getInt("total_inscritos");
                linha[5] = rs.getInt("total_presentes");
                lista.add(linha);
            }
        }
        return lista;
    }

    private Evento mapear(ResultSet rs) throws SQLException {
        Evento e = new Evento();
        e.setId(rs.getInt("id"));
        e.setTitulo(rs.getString("titulo"));
        e.setDescricao(rs.getString("descricao"));
        e.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        e.setLocalEvento(rs.getString("local_evento"));
        e.setCapacidade(rs.getInt("capacidade"));
        e.setCategoria(rs.getString("categoria"));
        e.setIdOrganizador(rs.getInt("id_organizador"));
        return e;
    }
}