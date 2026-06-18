import view.TelaPrincipal;
import javax.swing.*;

/**
 * Classe principal — ponto de entrada do sistema.
 *
 * SwingUtilities.invokeLater() garante que a GUI seja criada
 * na Event Dispatch Thread (EDT), que é a thread correta para
 * Swing segundo a documentação oficial do Java.
 *
 * UIManager.setLookAndFeel() define o visual do sistema operacional
 * para que o programa não pareça "feio" com o visual padrão do Java.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Se falhar, usa o visual padrão — não é erro crítico
            }
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}