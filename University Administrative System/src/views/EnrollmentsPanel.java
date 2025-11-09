package views;

import controller.EnrollmentController;
import model.Enrollment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnrollmentsPanel extends JPanel {
    private JTable enrollmentsTable;
    private DefaultTableModel tableModel;
    private JButton btnEnroll, btnUpdateGrade, btnDrop, btnRefresh;
    private EnrollmentController enrollmentController;

    public EnrollmentsPanel() {
        enrollmentController = new EnrollmentController();
        initComponents();
        loadEnrollments();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEnroll = new JButton("📝 Inscribir Estudiante");
        btnUpdateGrade = new JButton("📊 Actualizar Calificación");
        btnDrop = new JButton("📤 Dar de Baja");
        btnRefresh = new JButton("🔄 Actualizar");

        btnEnroll.addActionListener(e -> showEnrollStudentDialog());
        btnUpdateGrade.addActionListener(e -> showUpdateGradeDialog());
        btnDrop.addActionListener(e -> dropEnrollment());
        btnRefresh.addActionListener(e -> {
            loadEnrollments();
            refreshStats();
        });

        topPanel.add(btnEnroll);
        topPanel.add(btnUpdateGrade);
        topPanel.add(btnDrop);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        // Tabla de inscripciones
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Estudiante");
        tableModel.addColumn("Materia");
        tableModel.addColumn("Profesor");
        tableModel.addColumn("Fecha Inscripción");
        tableModel.addColumn("Calificación");
        tableModel.addColumn("Estado");
        tableModel.addColumn("Semestre");

        enrollmentsTable = new JTable(tableModel);
        enrollmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Inscripciones"));
        add(scrollPane, BorderLayout.CENTER);

        // Estadísticas
        add(createStatsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));

        try {
            int total = enrollmentController.getTotalEnrollments();
            int active = enrollmentController.getActiveEnrollments().size();
            int passed = enrollmentController.getPassedEnrollments().size();

            JLabel lblTotal = new JLabel("Total Inscripciones: " + total);
            JLabel lblActive = new JLabel("Activas: " + active);
            JLabel lblPassed = new JLabel("Aprobadas: " + passed);

            // Estilos para mejor visualización
            lblTotal.setForeground(Color.BLUE);
            lblActive.setForeground(new Color(0, 100, 0)); // Verde oscuro
            lblPassed.setForeground(Color.GREEN);

            statsPanel.add(lblTotal);
            statsPanel.add(Box.createHorizontalStrut(20));
            statsPanel.add(lblActive);
            statsPanel.add(Box.createHorizontalStrut(20));
            statsPanel.add(lblPassed);

        } catch (Exception e) {
            JLabel lblError = new JLabel("Error cargando estadísticas: " + e.getMessage());
            lblError.setForeground(Color.RED);
            statsPanel.add(lblError);
            e.printStackTrace(); // Para debug
        }

        return statsPanel;
    }

    public void loadEnrollments() {
        enrollmentController.loadEnrollmentsToTable(enrollmentsTable);
    }

    private void showEnrollStudentDialog() {
        JDialog dialog = new JDialog((Frame) null, "Inscribir Estudiante", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtStudentId = new JTextField();
        JTextField txtSubjectId = new JTextField();
        JTextField txtProfessorId = new JTextField();
        JTextField txtSemester = new JTextField("2024-01");

        formPanel.add(new JLabel("ID Estudiante:"));
        formPanel.add(txtStudentId);
        formPanel.add(new JLabel("ID Materia:"));
        formPanel.add(txtSubjectId);
        formPanel.add(new JLabel("ID Profesor:"));
        formPanel.add(txtProfessorId);
        formPanel.add(new JLabel("Semestre:"));
        formPanel.add(txtSemester);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Inscribir");
        JButton btnCancel = new JButton("Cancelar");

        btnSave.addActionListener(e -> {
            try {
                boolean success = enrollmentController.enrollStudent(
                        Integer.parseInt(txtStudentId.getText()),
                        Integer.parseInt(txtSubjectId.getText()),
                        Integer.parseInt(txtProfessorId.getText()),
                        txtSemester.getText()
                );

                if (success) {
                    loadEnrollments();
                    refreshStats();
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error en los IDs numéricos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showUpdateGradeDialog() {
        int selectedRow = enrollmentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una inscripción para actualizar calificación",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int enrollmentId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentGrade = tableModel.getValueAt(selectedRow, 5).toString();

        String newGrade = JOptionPane.showInputDialog(this,
                "Ingrese la nueva calificación (0-100):",
                currentGrade.equals("null") ? "" : currentGrade);

        if (newGrade != null && !newGrade.trim().isEmpty()) {
            try {
                double grade = Double.parseDouble(newGrade);
                boolean success = enrollmentController.updateGrade(enrollmentId, grade);
                if (success) {
                    loadEnrollments();
                    refreshStats();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "La calificación debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void dropEnrollment() {
        int selectedRow = enrollmentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una inscripción para dar de baja", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int enrollmentId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean success = enrollmentController.dropEnrollment(enrollmentId);

        if (success) {
            loadEnrollments();
            refreshStats();
        }
    }

    private void refreshStats() {
        JPanel statsPanel = createStatsPanel();

        // Reemplazar el panel de estadísticas existente
        BorderLayout layout = (BorderLayout) getLayout();
        Component oldStats = layout.getLayoutComponent(BorderLayout.SOUTH);
        if (oldStats != null) {
            remove(oldStats);
        }
        add(statsPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}