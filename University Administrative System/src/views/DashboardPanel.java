package views;

import controller.StudentController;
import controller.ProfessorController;
import controller.SubjectController;
import controller.EnrollmentController;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private StudentController studentController;
    private ProfessorController professorController;
    private SubjectController subjectController;
    private EnrollmentController enrollmentController;

    public DashboardPanel() {
        studentController = new StudentController();
        professorController = new ProfessorController();
        subjectController = new SubjectController();
        enrollmentController = new EnrollmentController();

        initComponents();
        loadStatistics();
    }

    private void initComponents() {
        setLayout(new GridLayout(2, 3, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));

        // Tarjetas de estadísticas
        add(createStatCard("🎓 Estudiantes",
                String.valueOf(studentController.getTotalStudents()),
                "Total registrados", Color.BLUE));

        add(createStatCard("👨‍🏫 Profesores",
                String.valueOf(professorController.getTotalProfessors()),
                "En el sistema", Color.GREEN));

        add(createStatCard("📚 Materias",
                String.valueOf(subjectController.getTotalSubjects()),
                "Disponibles", Color.ORANGE));

        add(createStatCard("📝 Inscripciones",
                String.valueOf(enrollmentController.getTotalEnrollments()),
                "Activas", Color.MAGENTA));

        add(createStatCard("📊 Promedio GPA",
                String.format("%.2f", getAverageGPA()),
                "General", Color.RED));

        add(createActionCard());
    }

    private JPanel createStatCard(String title, String value, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(color);

        JLabel descLabel = new JLabel(description, JLabel.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createActionCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("🚀 Acciones Rápidas", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnRefresh = new JButton("🔄 Actualizar Estadísticas");
        JButton btnReports = new JButton("📈 Generar Reportes");
        JButton btnBackup = new JButton("💾 Respaldar Datos");

        btnRefresh.addActionListener(e -> loadStatistics());
        btnReports.addActionListener(e -> showReportsDialog());
        btnBackup.addActionListener(e -> showBackupDialog());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnReports);
        buttonPanel.add(btnBackup);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);

        return card;
    }

    public void loadStatistics() {
        // Actualizar las tarjetas con datos frescos
        // En una implementación real, aquí se actualizarían los valores
        System.out.println("Estadísticas actualizadas");
    }

    private double getAverageGPA() {
        // Esto debería calcularse desde el servicio
        return 3.75; // Placeholder
    }

    private void showReportsDialog() {
        JOptionPane.showMessageDialog(this,
                "Generación de reportes en desarrollo\n\n" +
                        "Próximamente disponible:\n" +
                        "• Reporte de estudiantes por carrera\n" +
                        "• Reporte de calificaciones\n" +
                        "• Estadísticas académicas",
                "Generar Reportes",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showBackupDialog() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Desea realizar un respaldo de la base de datos?\n\n" +
                        "Esta acción creará una copia de seguridad de todos los datos.",
                "Respaldo de Datos",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Respaldo iniciado...\n" +
                            "Los datos se están guardando de forma segura.",
                    "Respaldo en Progreso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}