package view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela principal — menu de navegação do sistema.
 * Cada botão abre uma tela diferente em janela separada.
 * JFrame = janela
 * JPanel = painel (container de componentes)
 * JButton = botão
 * Layout = como os componentes são posicionados
 *   - BorderLayout: divide em 5 regiões (NORTH, SOUTH, EAST, WEST, CENTER)
 *   - GridLayout: organiza em linhas x colunas de tamanho igual
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        // Configurações básicas da janela
        setTitle("Sistema de Eventos Comunitarios");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fecha o programa ao fechar a janela
        setLocationRelativeTo(null);                     // centraliza na tela
        setResizable(false);                             // impede redimensionamento

        // Painel principal com BorderLayout
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        painelPrincipal.setBackground(new Color(245, 245, 245));

        // Título no topo
        JLabel lblTitulo = new JLabel("Prefeitura Comunitaria", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Painel de botões no centro (4 linhas, 1 coluna)
        JPanel painelBotoes = new JPanel(new GridLayout(4, 1, 10, 10));
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JButton btnUsuarios  = new JButton("Gerenciar Usuarios");
        JButton btnEventos   = new JButton("Gerenciar Eventos");
        JButton btnInscricao = new JButton("Inscricoes e Presenca");
        JButton btnRelatorio = new JButton("Relatorios");

        painelBotoes.add(btnUsuarios);
        painelBotoes.add(btnEventos);
        painelBotoes.add(btnInscricao);
        painelBotoes.add(btnRelatorio);

        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);

        // Rodapé
        JLabel lblRodape = new JLabel("CEFET-RJ - POO - Prof. Rafael Costa", SwingConstants.CENTER);
        lblRodape.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblRodape.setForeground(Color.GRAY);
        painelPrincipal.add(lblRodape, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Ações: cada botão abre sua tela em nova janela
        // DISPOSE_ON_CLOSE fecha só a janela filha, não o programa inteiro
        btnUsuarios.addActionListener(e -> new TelaGerenciamentoUsuarios().setVisible(true));
        btnEventos.addActionListener(e -> new TelaGerenciamentoEventos().setVisible(true));
        btnInscricao.addActionListener(e -> new TelaInscricao().setVisible(true));
        btnRelatorio.addActionListener(e -> new TelaRelatorio().setVisible(true));
    }
}