import view.TelaPrincipal;
import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                
            }
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}