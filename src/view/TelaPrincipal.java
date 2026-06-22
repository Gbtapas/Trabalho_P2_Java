package view;

import javax.swing.*;
import java.awt.*;


public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        
        setTitle("Sistema de Eventos Comunitarios");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);                     
        setResizable(false);                             

        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        painelPrincipal.setBackground(new Color(245, 245, 245));

        
        JLabel lblTitulo = new JLabel("Prefeitura Comunitaria", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        
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

        
        JLabel lblRodape = new JLabel("CEFET-RJ - POO - Prof. Rafael Costa", SwingConstants.CENTER);
        lblRodape.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblRodape.setForeground(Color.GRAY);
        painelPrincipal.add(lblRodape, BorderLayout.SOUTH);

        add(painelPrincipal);

        
        
        btnUsuarios.addActionListener(e -> new TelaGerenciamentoUsuarios().setVisible(true));
        btnEventos.addActionListener(e -> new TelaGerenciamentoEventos().setVisible(true));
        btnInscricao.addActionListener(e -> new TelaInscricao().setVisible(true));
        btnRelatorio.addActionListener(e -> new TelaRelatorio().setVisible(true));
    }
}