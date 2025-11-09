package views;

import model.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionConfigDialog extends JDialog {
    private JTextField txtUrl, txtUser, txtPassword;
    private JButton btnTest, btnSave;

    public ConnectionConfigDialog(JFrame parent) {
        super(parent, "Configuración de Conexión", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 200);
        setResizable(false);

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("URL:"));
        txtUrl = new JTextField("jdbc:oracle:thin:@localhost:1521:XE");
        panelForm.add(txtUrl);

        panelForm.add(new JLabel("Usuario:"));
        txtUser = new JTextField("university");
        panelForm.add(txtUser);

        panelForm.add(new JLabel("Password:"));
        txtPassword = new JPasswordField("university123");
        panelForm.add(txtPassword);

        panelForm.add(new JLabel());
        JPanel panelButtons = new JPanel(new FlowLayout());
        btnTest = new JButton("Probar Conexión");
        btnSave = new JButton("Guardar");

        panelButtons.add(btnTest);
        panelButtons.add(btnSave);
        panelForm.add(panelButtons);

        add(panelForm, BorderLayout.CENTER);

        configureEvents();
    }

    private void configureEvents() {
        btnTest.addActionListener(e -> testConnection());
        btnSave.addActionListener(e -> dispose());
    }

    private void testConnection() {
        try {
            String url = txtUrl.getText();
            String user = txtUser.getText();
            String password = txtPassword.getText();

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(url, user, password);

            JOptionPane.showMessageDialog(this, "¡Conexión exitosa a Oracle 11g!");
            conn.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de conexión:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}