package view;

import business.NegocioException;
import business.RegrasNegocio;
import dao.EventoDAO;
import dao.UsuarioDAO;
import model.Evento;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class TelaGerenciamentoEventos extends JFrame {

    private JTextField txtTitulo, txtDescricao, txtDataHora, txtLocal, txtCapacidade;
    private JComboBox<String> cbxCategoria;
    private JComboBox<Usuario> cbxOrganizador; 
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnSalvar, btnExcluir, btnLimpar;
    private int idSelecionado = 0;

    private final EventoDAO eventoDAO = new EventoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public TelaGerenciamentoEventos() {
        setTitle("Gerenciamento de Eventos");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel painelForm = new JPanel(new GridLayout(7, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Evento"));

        painelForm.add(new JLabel("Titulo:"));
        txtTitulo = new JTextField();
        painelForm.add(txtTitulo);

        painelForm.add(new JLabel("Descricao:"));
        txtDescricao = new JTextField();
        painelForm.add(txtDescricao);

        painelForm.add(new JLabel("Data/Hora (dd/MM/yyyy HH:mm):"));
        txtDataHora = new JTextField();
        painelForm.add(txtDataHora);

        painelForm.add(new JLabel("Local:"));
        txtLocal = new JTextField();
        painelForm.add(txtLocal);

        painelForm.add(new JLabel("Capacidade:"));
        txtCapacidade = new JTextField();
        painelForm.add(txtCapacidade);

        painelForm.add(new JLabel("Categoria:"));
        cbxCategoria = new JComboBox<>(new String[]{"PALESTRA", "OFICINA", "FEIRA"});
        painelForm.add(cbxCategoria);

        painelForm.add(new JLabel("Organizador:"));
        cbxOrganizador = new JComboBox<>();
        painelForm.add(cbxOrganizador);

        painel.add(painelForm, BorderLayout.NORTH);
        carregarOrganizadores();

        
        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Titulo", "Categoria", "Data/Hora", "Local", "Capac.", "Organizador"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                preencherFormulario();
            }
        });

        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar  = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        btnLimpar  = new JButton("Limpar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparFormulario());

        add(painel);
        carregarTabela();
    }

    
    private void carregarOrganizadores() {
        try {
            List<Usuario> orgs = usuarioDAO.buscarPorTipo("ORGANIZADOR");
            cbxOrganizador.removeAllItems();
            for (Usuario u : orgs) {
                cbxOrganizador.addItem(u); 
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar organizadores: " + ex.getMessage());
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Evento> lista = eventoDAO.listarTodos();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Evento ev : lista) {
                Usuario org = usuarioDAO.buscarPorId(ev.getIdOrganizador());
                String nomeOrg = org != null ? org.getNome() : "N/A";
                modeloTabela.addRow(new Object[]{
                        ev.getId(), ev.getTitulo(), ev.getCategoria(),
                        ev.getDataHora().format(fmt), ev.getLocalEvento(),
                        ev.getCapacidade(), nomeOrg
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage());
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtTitulo.setText((String) modeloTabela.getValueAt(linha, 1));
        cbxCategoria.setSelectedItem(modeloTabela.getValueAt(linha, 2));
        txtDataHora.setText((String) modeloTabela.getValueAt(linha, 3));
        txtLocal.setText((String) modeloTabela.getValueAt(linha, 4));
        txtCapacidade.setText(String.valueOf(modeloTabela.getValueAt(linha, 5)));
        try {
            Evento ev = eventoDAO.buscarPorId(idSelecionado);
            if (ev != null) {
                txtDescricao.setText(ev.getDescricao());
                for (int i = 0; i < cbxOrganizador.getItemCount(); i++) {
                    if (cbxOrganizador.getItemAt(i).getId() == ev.getIdOrganizador()) {
                        cbxOrganizador.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    
    private void salvar() {
        if (txtTitulo.getText().trim().isEmpty() || txtLocal.getText().trim().isEmpty()
                || txtDataHora.getText().trim().isEmpty() || txtCapacidade.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatorios.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(txtDataHora.getText().trim(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data invalido. Use: dd/MM/yyyy HH:mm",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacidade;
        try {
            capacidade = Integer.parseInt(txtCapacidade.getText().trim());
            if (capacidade <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacidade deve ser um numero positivo.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cbxOrganizador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um organizador.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Evento ev = new Evento();
        ev.setTitulo(txtTitulo.getText().trim());
        ev.setDescricao(txtDescricao.getText().trim());
        ev.setDataHora(dataHora);
        ev.setLocalEvento(txtLocal.getText().trim());
        ev.setCapacidade(capacidade);
        ev.setCategoria((String) cbxCategoria.getSelectedItem());
        ev.setIdOrganizador(((Usuario) cbxOrganizador.getSelectedItem()).getId());

        try {
            
            RegrasNegocio.verificarConflitoHorario(ev.getLocalEvento(), ev.getDataHora(), idSelecionado);

            if (idSelecionado == 0) {
                eventoDAO.inserir(ev);
                JOptionPane.showMessageDialog(this, "Evento cadastrado!");
            } else {
                ev.setId(idSelecionado);
                eventoDAO.atualizar(ev);
                JOptionPane.showMessageDialog(this, "Evento atualizado!");
            }
            limparFormulario();
            carregarTabela();

        } catch (NegocioException ne) {
            JOptionPane.showMessageDialog(this, ne.getMessage(),
                    "Regra de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um evento na tabela.");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this,
                "Deseja excluir este evento?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                eventoDAO.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Evento excluido.");
                limparFormulario();
                carregarTabela();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir (evento pode ter inscricoes): " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparFormulario() {
        idSelecionado = 0;
        txtTitulo.setText("");
        txtDescricao.setText("");
        txtDataHora.setText("");
        txtLocal.setText("");
        txtCapacidade.setText("");
        cbxCategoria.setSelectedIndex(0);
        if (cbxOrganizador.getItemCount() > 0) cbxOrganizador.setSelectedIndex(0);
        tabela.clearSelection();
    }
}