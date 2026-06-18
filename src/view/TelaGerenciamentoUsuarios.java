package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela CRUD de usuários.
 *
 * COMPONENTES SWING usados:
 *  JTextField    → caixa de texto (nome, email, telefone)
 *  JComboBox     → lista suspensa (tipo: ORGANIZADOR, VOLUNTARIO, PUBLICO)
 *  JTable        → tabela com linhas e colunas
 *  JButton       → botões de ação
 *  JLabel        → rótulos dos campos
 *  JOptionPane   → caixas de diálogo (mensagens, confirmação)
 * LÓGICA: ao clicar numa linha da tabela, os dados vão para o formulário.
 *         "Salvar" decide entre INSERT (id=0) ou UPDATE (id>0).
 */
public class TelaGerenciamentoUsuarios extends JFrame {

    private JTextField txtNome, txtEmail, txtTelefone;
    private JComboBox<String> cbxTipo;
    private JTable tabela;
    private DefaultTableModel modeloTabela; // modelo = dados da tabela
    private JButton btnSalvar, btnExcluir, btnLimpar;
    private int idSelecionado = 0; // 0 = novo cadastro, >0 = editando

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public TelaGerenciamentoUsuarios() {
        setTitle("Gerenciamento de Usuarios");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== FORMULÁRIO (topo) =====
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Usuario"));

        painelForm.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelForm.add(txtNome);

        painelForm.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        painelForm.add(txtEmail);

        painelForm.add(new JLabel("Tipo:"));
        cbxTipo = new JComboBox<>(new String[]{"ORGANIZADOR", "VOLUNTARIO", "PUBLICO"});
        painelForm.add(cbxTipo);

        painelForm.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelForm.add(txtTelefone);

        painel.add(painelForm, BorderLayout.NORTH);

        // ===== TABELA (centro) =====
        // DefaultTableModel gerencia os dados da JTable
        // Sobrescrevemos isCellEditable para a tabela ser SOMENTE LEITURA
        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Nome", "E-mail", "Tipo", "Telefone"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // uma linha por vez
        JScrollPane scroll = new JScrollPane(tabela);
        painel.add(scroll, BorderLayout.CENTER);

        // Listener: quando clica numa linha, preenche o formulário
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                preencherFormulario();
            }
        });

        // ===== BOTÕES (rodapé) =====
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar  = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        btnLimpar  = new JButton("Limpar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparFormulario());

        add(painel);
        carregarTabela(); // popula a tabela ao abrir a tela
    }

    /** Lê todos os usuários do banco e preenche a tabela */
    private void carregarTabela() {
        modeloTabela.setRowCount(0); // limpa a tabela
        try {
            List<Usuario> lista = usuarioDAO.listarTodos();
            for (Usuario u : lista) {
                modeloTabela.addRow(new Object[]{
                        u.getId(), u.getNome(), u.getEmail(), u.getTipo(), u.getTelefone()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Quando clica na tabela, copia os dados para o formulário */
    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtEmail.setText((String) modeloTabela.getValueAt(linha, 2));
        cbxTipo.setSelectedItem(modeloTabela.getValueAt(linha, 3));
        txtTelefone.setText((String) modeloTabela.getValueAt(linha, 4));
    }

    /** Salva: se idSelecionado==0 faz INSERT, senão faz UPDATE */
    private void salvar() {
        if (txtNome.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e E-mail sao obrigatorios.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setNome(txtNome.getText().trim());
        u.setEmail(txtEmail.getText().trim());
        u.setTipo((String) cbxTipo.getSelectedItem());
        u.setTelefone(txtTelefone.getText().trim());

        try {
            if (idSelecionado == 0) {
                usuarioDAO.inserir(u);
                JOptionPane.showMessageDialog(this, "Usuario cadastrado!");
            } else {
                u.setId(idSelecionado);
                usuarioDAO.atualizar(u);
                JOptionPane.showMessageDialog(this, "Usuario atualizado!");
            }
            limparFormulario();
            carregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Exclui o usuário selecionado */
    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuario na tabela.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this,
                "Deseja excluir este usuario?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                usuarioDAO.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Usuario excluido.");
                limparFormulario();
                carregarTabela();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir (usuario pode ter inscricoes): " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Limpa formulário e volta ao modo "novo cadastro" */
    private void limparFormulario() {
        idSelecionado = 0;
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        cbxTipo.setSelectedIndex(0);
        tabela.clearSelection();
    }
}