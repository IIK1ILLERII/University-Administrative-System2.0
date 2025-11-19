package views;

import controller.SubjectController;
import model.Subject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;
import java.util.List;

public class SubjectsPanel extends JPanel {
    private JTable subjectsTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private SubjectController subjectController;

    public SubjectsPanel() {
        subjectController = new SubjectController();
        initComponents();
        loadSubjects();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Agregar Materia");
        btnEdit = new JButton("Editar");
        btnDelete = new JButton("Eliminar");
        btnRefresh = new JButton("Actualizar");

        btnAdd.addActionListener(e -> showAddSubjectDialog());
        btnEdit.addActionListener(e -> showEditSubjectDialog());
        btnDelete.addActionListener(e -> deleteSubject());
        btnRefresh.addActionListener(e -> loadSubjects());

        topPanel.add(btnAdd);
        topPanel.add(btnEdit);
        topPanel.add(btnDelete);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Código");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Créditos");
        tableModel.addColumn("Profesor");
        tableModel.addColumn("Departamento");
        tableModel.addColumn("Dificultad");
        tableModel.addColumn("Horas/Semana");

        subjectsTable = new JTable(tableModel);
        subjectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(subjectsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Materias"));
        add(scrollPane, BorderLayout.CENTER);

        add(createStatsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));

        int total = subjectController.getTotalSubjects();

        JLabel lblTotal = new JLabel("Total Materias: " + total);
        JLabel lblBasic = new JLabel("Básicas: " + subjectController.getBasicSubjects().size());
        JLabel lblIntermediate = new JLabel("Intermedias: " + subjectController.getIntermediateSubjects().size());
        JLabel lblAdvanced = new JLabel("Avanzadas: " + subjectController.getAdvancedSubjects().size());

        statsPanel.add(lblTotal);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblBasic);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblIntermediate);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblAdvanced);

        return statsPanel;
    }

    public void loadSubjects() {
        subjectController.loadSubjectsToTable(subjectsTable);
    }

    private void showAddSubjectDialog() {
        JDialog dialog = new JDialog((Frame) null, "Agregar Nueva Materia", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtSubjectName = new JTextField();
        JTextField txtCode = new JTextField();
        JTextField txtCredits = new JTextField("3");
        JTextField txtProfessorId = new JTextField();
        JTextField txtDepartment = new JTextField();
        JComboBox<String> comboDifficulty = new JComboBox<>(
                new String[]{"BASIC", "INTERMEDIATE", "ADVANCED"});
        JTextField txtHours = new JTextField("4");

        formPanel.add(new JLabel("Nombre Materia:"));
        formPanel.add(txtSubjectName);
        formPanel.add(new JLabel("Código:"));
        formPanel.add(txtCode);
        formPanel.add(new JLabel("Créditos:"));
        formPanel.add(txtCredits);
        formPanel.add(new JLabel("ID Profesor:"));
        formPanel.add(txtProfessorId);
        formPanel.add(new JLabel("Departamento:"));
        formPanel.add(txtDepartment);
        formPanel.add(new JLabel("Dificultad:"));
        formPanel.add(comboDifficulty);
        formPanel.add(new JLabel("Horas/Semana:"));
        formPanel.add(txtHours);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");

        btnSave.addActionListener(e -> {
            try {
                boolean success = subjectController.addSubject(
                        txtSubjectName.getText(),
                        txtCode.getText(),
                        Integer.parseInt(txtCredits.getText()),
                        txtProfessorId.getText().isEmpty() ? 0 : Integer.parseInt(txtProfessorId.getText()),
                        txtDepartment.getText(),
                        comboDifficulty.getSelectedItem().toString(),
                        Integer.parseInt(txtHours.getText())
                );

                if (success) {
                    loadSubjects();
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error en los datos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditSubjectDialog() {
        int selectedRow = subjectsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una materia para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int subjectId = (int) tableModel.getValueAt(selectedRow, 0);
        Optional<Subject> subjectOpt = subjectController.getSubjectById(subjectId);

        if (subjectOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar los datos de la materia", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Subject subject = subjectOpt.get();
        JDialog dialog = new JDialog((Frame) null, "Editar Materia", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtSubjectName = new JTextField(subject.getSubjectName());
        JTextField txtCode = new JTextField(subject.getCode());
        JTextField txtCredits = new JTextField(String.valueOf(subject.getCredits()));
        JTextField txtProfessorId = new JTextField(subject.getProfessorId() > 0 ?
                String.valueOf(subject.getProfessorId()) : "");
        JTextField txtDepartment = new JTextField(subject.getDepartment());
        JComboBox<String> comboDifficulty = new JComboBox<>(
                new String[]{"BASIC", "INTERMEDIATE", "ADVANCED"});
        comboDifficulty.setSelectedItem(subject.getDifficultyLevel());
        JTextField txtHours = new JTextField(String.valueOf(subject.getHoursPerWeek()));

        formPanel.add(new JLabel("Nombre Materia:"));
        formPanel.add(txtSubjectName);
        formPanel.add(new JLabel("Código:"));
        formPanel.add(txtCode);
        formPanel.add(new JLabel("Créditos:"));
        formPanel.add(txtCredits);
        formPanel.add(new JLabel("ID Profesor:"));
        formPanel.add(txtProfessorId);
        formPanel.add(new JLabel("Departamento:"));
        formPanel.add(txtDepartment);
        formPanel.add(new JLabel("Dificultad:"));
        formPanel.add(comboDifficulty);
        formPanel.add(new JLabel("Horas/Semana:"));
        formPanel.add(txtHours);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Guardar Cambios");
        JButton btnCancel = new JButton("Cancelar");

        btnSave.addActionListener(e -> {
            try {
                boolean success = subjectController.updateSubject(
                        subjectId,
                        txtSubjectName.getText(),
                        txtCode.getText(),
                        Integer.parseInt(txtCredits.getText()),
                        txtProfessorId.getText().isEmpty() ? 0 : Integer.parseInt(txtProfessorId.getText()),
                        txtDepartment.getText(),
                        comboDifficulty.getSelectedItem().toString(),
                        Integer.parseInt(txtHours.getText())
                );

                if (success) {
                    loadSubjects();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this,
                            "Materia actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error en los datos numéricos (créditos, ID profesor u horas)",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSubject() {
        int selectedRow = subjectsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una materia para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int subjectId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean success = subjectController.deleteSubject(subjectId);

        if (success) {
            loadSubjects();
        }
    }
}