import views.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                UIManager.put("Button.arc", 20);
                UIManager.put("Component.arc", 20);
                UIManager.put("TextComponent.arc", 10);

            } catch (Exception e) {
                System.err.println("Error setting look and feel: " + e.getMessage());
            }

            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}