package view;

import dao.EventoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de relatórios.
 * Exibe uma tabela consolidada com: evento, categoria, data,
 * capacidade, total de inscritos e total de presentes.
 *
 * Os dados vêm do método gerarRelatorio() do EventoDAO,
 * que faz um JOIN entre 3 tabelas.
 */
public class TelaRelatorio extends JFrame {

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaRelatorio() {
        setTitle("Relatorio de Eventos");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Resumo dos Eventos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        painel.add(lblTitulo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(
                new String[]{"Evento", "Categoria", "Data/Hora", "Capacidade", "Inscritos", "Presentes"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(painel);
        carregarRelatorio();
    }

    private void carregarRelatorio() {
        modeloTabela.setRowCount(0);
        try {
            EventoDAO eventoDAO = new EventoDAO();
            List<Object[]> relatorio = eventoDAO.gerarRelatorio();
            for (Object[] linha : relatorio) {
                modeloTabela.addRow(linha);
            }
            if (relatorio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum evento encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatorio: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}