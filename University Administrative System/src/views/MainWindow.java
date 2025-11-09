package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;

    public MainWindow() {
        setTitle("Sistema Administrativo Universitario - Oracle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initComponents();
        createMenu();
        setupWindowListener();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // Crear paneles con íconos
        tabbedPane.addTab("🏠 Dashboard", new DashboardPanel());
        tabbedPane.addTab("🎓 Estudiantes", new StudentsPanel());
        tabbedPane.addTab("👨‍🏫 Profesores", new ProfessorsPanel());
        tabbedPane.addTab("📚 Materias", new SubjectsPanel());
        tabbedPane.addTab("📝 Inscripciones", new EnrollmentsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setPreferredSize(new Dimension(getWidth(), 25));

        JLabel statusLabel = new JLabel(" Conectado a Oracle Database | Sistema Universitario v2.0");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        JLabel timeLabel = new JLabel(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(timeLabel, BorderLayout.EAST);

        // Actualizar hora cada segundo
        new Timer(1000, e -> {
            timeLabel.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
        }).start();

        return statusPanel;
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu fileMenu = new JMenu("Archivo");
        fileMenu.setMnemonic(KeyEvent.VK_A);

        JMenuItem newStudentItem = new JMenuItem("Nuevo Estudiante");
        newStudentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newStudentItem.addActionListener(e -> showNewStudentDialog());

        JMenuItem newProfessorItem = new JMenuItem("Nuevo Profesor");
        newProfessorItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        newProfessorItem.addActionListener(e -> showNewProfessorDialog());

        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> exitApplication());

        fileMenu.add(newStudentItem);
        fileMenu.add(newProfessorItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Menú Herramientas
        JMenu toolsMenu = new JMenu("Herramientas");
        toolsMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem connectionItem = new JMenuItem("Configurar Conexión");
        connectionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        connectionItem.addActionListener(e -> showConnectionDialog());

        JMenuItem backupItem = new JMenuItem("Respaldar Base de Datos");
        backupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK));
        backupItem.addActionListener(e -> backupDatabase());

        JMenuItem refreshItem = new JMenuItem("Actualizar Datos");
        refreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        refreshItem.addActionListener(e -> refreshData());

        toolsMenu.add(connectionItem);
        toolsMenu.add(backupItem);
        toolsMenu.addSeparator();
        toolsMenu.add(refreshItem);

        // Menú Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        helpMenu.setMnemonic(KeyEvent.VK_Y);

        JMenuItem aboutItem = new JMenuItem("Acerca de");
        aboutItem.addActionListener(e -> showAboutDialog());

        JMenuItem helpItem = new JMenuItem("Manual de Usuario");
        helpItem.addActionListener(e -> showHelp());

        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void setupWindowListener() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitApplication();
            }
        });
    }

    // MÉTODOS DE LOS MENÚS
    private void showNewStudentDialog() {
        JOptionPane.showMessageDialog(this,
                "Función: Agregar nuevo estudiante\n(Se implementará en el panel de estudiantes)",
                "Nuevo Estudiante",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showNewProfessorDialog() {
        JOptionPane.showMessageDialog(this,
                "Función: Agregar nuevo profesor\n(Se implementará en el panel de profesores)",
                "Nuevo Profesor",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showConnectionDialog() {
        JOptionPane.showMessageDialog(this,
                "Configuración de conexión a base de datos\n(Se implementará posteriormente)",
                "Configurar Conexión",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void backupDatabase() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Desea realizar un respaldo de la base de datos?",
                "Respaldo de Base de Datos",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Respaldo iniciado...\nEsta función está en desarrollo.",
                    "Respaldo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshData() {
        // Refrescar todos los paneles
        Component[] components = tabbedPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof DashboardPanel) {
                ((DashboardPanel) comp).loadStatistics();
            } else if (comp instanceof StudentsPanel) {
                ((StudentsPanel) comp).loadStudents();
            } else if (comp instanceof ProfessorsPanel) {
                ((ProfessorsPanel) comp).loadProfessors();
            } else if (comp instanceof SubjectsPanel) {
                ((SubjectsPanel) comp).loadSubjects();
            } else if (comp instanceof EnrollmentsPanel) {
                ((EnrollmentsPanel) comp).loadEnrollments();
            }
        }

        JOptionPane.showMessageDialog(this,
                "Datos actualizados correctamente",
                "Actualización",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Sistema Administrativo Universitario v2.0\n\n" +
                        "Desarrollado con Java Swing y Oracle Database\n" +
                        "Arquitectura MVC + DAO\n" +
                        "© 2024 - Sistema Universitario",
                "Acerca del Sistema",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this,
                "Manual de Usuario del Sistema Universitario\n\n" +
                        "1. Dashboard: Ver estadísticas generales\n" +
                        "2. Estudiantes: Gestionar estudiantes\n" +
                        "3. Profesores: Gestionar profesores\n" +
                        "4. Materias: Gestionar materias\n" +
                        "5. Inscripciones: Gestionar inscripciones\n\n" +
                        "Use los atajos de teclado para acceder rápidamente a las funciones.",
                "Ayuda del Sistema",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea salir del sistema?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.DatabaseConnection.closeConnection();
            System.exit(0);
        }
    }

    // Método para cambiar entre pestañas
    public void showTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(tabIndex);
        }
    }

    // Método para obtener el panel actual
    public Component getCurrentPanel() {
        return tabbedPane.getSelectedComponent();
    }
}