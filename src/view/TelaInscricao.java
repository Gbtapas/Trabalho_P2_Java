package view;

import business.NegocioException;
import business.RegrasNegocio;
import dao.EventoDAO;
import dao.InscricaoDAO;
import dao.ParticipacaoDAO;
import dao.UsuarioDAO;
import model.Evento;
import model.Inscricao;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;


public class TelaInscricao extends JFrame {

    private JComboBox<Evento> cbxEvento;
    private JComboBox<Usuario> cbxUsuario;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnInscrever, btnRegistrarPresenca, btnCancelarInscricao;

    private final EventoDAO eventoDAO = new EventoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final InscricaoDAO inscricaoDAO = new InscricaoDAO();
    private final ParticipacaoDAO participacaoDAO = new ParticipacaoDAO();

    public TelaInscricao() {
        setTitle("Inscricoes e Presenca");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel painelSel = new JPanel(new GridLayout(2, 2, 5, 5));
        painelSel.setBorder(BorderFactory.createTitledBorder("Nova Inscricao"));

        painelSel.add(new JLabel("Evento:"));
        cbxEvento = new JComboBox<>();
        painelSel.add(cbxEvento);

        painelSel.add(new JLabel("Participante (Publico/Voluntario):"));
        cbxUsuario = new JComboBox<>();
        painelSel.add(cbxUsuario);

        painel.add(painelSel, BorderLayout.NORTH);

        carregarEventos();
        carregarUsuarios();

        
        cbxEvento.addActionListener(e -> carregarTabelaInscritos());

        
        modeloTabela = new DefaultTableModel(
                new String[]{"ID Inscricao", "Nome", "Tipo", "Data Inscricao", "Presente"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnInscrever         = new JButton("Inscrever Participante");
        btnRegistrarPresenca = new JButton("Registrar Presenca");
        btnCancelarInscricao = new JButton("Cancelar Inscricao");
        painelBotoes.add(btnInscrever);
        painelBotoes.add(btnRegistrarPresenca);
        painelBotoes.add(btnCancelarInscricao);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        btnInscrever.addActionListener(e -> inscrever());
        btnRegistrarPresenca.addActionListener(e -> registrarPresenca());
        btnCancelarInscricao.addActionListener(e -> cancelarInscricao());

        add(painel);
        carregarTabelaInscritos();
    }

    private void carregarEventos() {
        try {
            List<Evento> eventos = eventoDAO.listarTodos();
            cbxEvento.removeAllItems();
            for (Evento ev : eventos) cbxEvento.addItem(ev);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar eventos: " + ex.getMessage());
        }
    }

    private void carregarUsuarios() {
        try {
            cbxUsuario.removeAllItems();
            
            for (Usuario u : usuarioDAO.buscarPorTipo("PUBLICO"))    cbxUsuario.addItem(u);
            for (Usuario u : usuarioDAO.buscarPorTipo("VOLUNTARIO")) cbxUsuario.addItem(u);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuarios: " + ex.getMessage());
        }
    }

    private void carregarTabelaInscritos() {
        modeloTabela.setRowCount(0);
        Evento ev = (Evento) cbxEvento.getSelectedItem();
        if (ev == null) return;
        try {
            List<Object[]> detalhes = inscricaoDAO.listarDetalhesPorEvento(ev.getId());
            for (Object[] row : detalhes) modeloTabela.addRow(row);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar inscritos: " + ex.getMessage());
        }
    }

    
    private void inscrever() {
        Evento evento = (Evento) cbxEvento.getSelectedItem();
        Usuario usuario = (Usuario) cbxUsuario.getSelectedItem();

        if (evento == null || usuario == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento e um participante.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            
            RegrasNegocio.verificarInscricaoDuplicada(usuario.getId(), evento.getId());
            RegrasNegocio.verificarLotacao(evento.getId());

            
            Inscricao insc = new Inscricao(usuario.getId(), evento.getId());
            inscricaoDAO.inserir(insc);
            JOptionPane.showMessageDialog(this, "Inscricao realizada!");
            carregarTabelaInscritos();

        } catch (NegocioException ne) {
            JOptionPane.showMessageDialog(this, ne.getMessage(),
                    "Regra de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao inscrever: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void registrarPresenca() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um inscrito na tabela.");
            return;
        }
        int idInscricao = (int) modeloTabela.getValueAt(linha, 0);

        
        String[] opcoes = {"Presente", "Ausente"};
        int escolha = JOptionPane.showOptionDialog(this, "Registrar presenca:",
                "Presenca", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);

        boolean presente = (escolha == 0);
        String obs = JOptionPane.showInputDialog(this, "Observacao (opcional):");

        try {
            participacaoDAO.registrarPresenca(idInscricao, presente, obs);
            JOptionPane.showMessageDialog(this, "Presenca registrada!");
            carregarTabelaInscritos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void cancelarInscricao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma inscricao na tabela.");
            return;
        }
        int idInscricao = (int) modeloTabela.getValueAt(linha, 0);
        int conf = JOptionPane.showConfirmDialog(this,
                "Cancelar esta inscricao?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                inscricaoDAO.excluir(idInscricao);
                JOptionPane.showMessageDialog(this, "Inscricao cancelada.");
                carregarTabelaInscritos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}