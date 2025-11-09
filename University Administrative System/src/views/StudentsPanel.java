package views;

import controller.StudentController;
import model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class StudentsPanel extends JPanel {
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> comboSearchType;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;
    private StudentController studentController;

    public StudentsPanel() {
        studentController = new StudentController();
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior - Búsqueda y botones
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        comboSearchType = new JComboBox<>(new String[]{"Nombre", "Carrera", "Email", "Semestre"});
        searchPanel.add(comboSearchType);

        btnSearch = new JButton("🔍 Buscar");
        btnSearch.addActionListener(e -> searchStudents());
        searchPanel.add(btnSearch);

        topPanel.add(searchPanel, BorderLayout.WEST);

        // Panel de botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("➕ Agregar");
        btnEdit = new JButton("✏️ Editar");
        btnDelete = new JButton("🗑️ Eliminar");
        btnRefresh = new JButton("🔄 Actualizar");

        btnAdd.addActionListener(e -> showAddStudentDialog());
        btnEdit.addActionListener(e -> showEditStudentDialog());
        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> loadStudents());

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        topPanel.add(actionPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Tabla de estudiantes
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Apellido");
        tableModel.addColumn("Email");
        tableModel.addColumn("Teléfono");
        tableModel.addColumn("Carrera");
        tableModel.addColumn("Semestre");
        tableModel.addColumn("GPA");
        tableModel.addColumn("Fecha Inscripción");
        tableModel.addColumn("Estado");

        studentsTable = new JTable(tableModel);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Nombre
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Apellido
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Email
        studentsTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Teléfono
        studentsTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Carrera
        studentsTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Semestre
        studentsTable.getColumnModel().getColumn(7).setPreferredWidth(60);  // GPA
        studentsTable.getColumnModel().getColumn(8).setPreferredWidth(100); // Fecha
        studentsTable.getColumnModel().getColumn(9).setPreferredWidth(80);  // Estado

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Estudiantes"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de estadísticas
        add(createStatsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));

        int total = studentController.getTotalStudents();
        int active = studentController.getActiveStudents().size();

        JLabel lblTotal = new JLabel("Total: " + total);
        JLabel lblActive = new JLabel("Activos: " + active);
        JLabel lblInactive = new JLabel("Inactivos: " + (total - active));

        statsPanel.add(lblTotal);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblActive);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblInactive);

        return statsPanel;
    }

    public void loadStudents() {
        studentController.loadStudentsToTable(studentsTable);
        updateStats();
    }

    private void updateStats() {
        // Actualizar estadísticas si es necesario
    }

    private void searchStudents() {
        String criteria = txtSearch.getText().trim();
        String searchType = comboSearchType.getSelectedItem().toString();

        if (criteria.isEmpty()) {
            loadStudents();
            return;
        }

        List<Student> students = studentController.searchStudents(criteria, searchType);
        tableModel.setRowCount(0);

        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getCareer(),
                    student.getSemester(),
                    String.format("%.2f", student.getGpa()),
                    student.getEnrollmentDate(),
                    student.getStatus()
            });
        }

        JOptionPane.showMessageDialog(this,
                "Se encontraron " + students.size() + " estudiantes",
                "Resultados de Búsqueda",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog((Frame) null, "Agregar Nuevo Estudiante", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtCareer = new JTextField();
        JTextField txtSemester = new JTextField();
        JTextField txtDateOfBirth = new JTextField();
        JTextField txtGPA = new JTextField("0.0");

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtFirstName);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtLastName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Carrera:"));
        formPanel.add(txtCareer);
        formPanel.add(new JLabel("Semestre:"));
        formPanel.add(txtSemester);
        formPanel.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):"));
        formPanel.add(txtDateOfBirth);
        formPanel.add(new JLabel("GPA Inicial:"));
        formPanel.add(txtGPA);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");

        btnSave.addActionListener(e -> {
            try {
                boolean success = studentController.addStudent(
                        txtFirstName.getText(),
                        txtLastName.getText(),
                        txtEmail.getText(),
                        txtPhone.getText(),
                        txtCareer.getText(),
                        Integer.parseInt(txtSemester.getText()),
                        txtDateOfBirth.getText(),
                        Double.parseDouble(txtGPA.getText())
                );

                if (success) {
                    loadStudents();
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

    private void showEditStudentDialog() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un estudiante para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        Optional<Student> studentOpt = studentController.getStudentById(studentId);

        if (studentOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar los datos del estudiante", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = studentOpt.get();
        JDialog dialog = new JDialog((Frame) null, "Editar Estudiante", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtFirstName = new JTextField(student.getFirstName());
        JTextField txtLastName = new JTextField(student.getLastName());
        JTextField txtEmail = new JTextField(student.getEmail());
        JTextField txtPhone = new JTextField(student.getPhone());
        JTextField txtCareer = new JTextField(student.getCareer());
        JTextField txtSemester = new JTextField(String.valueOf(student.getSemester()));
        JTextField txtDateOfBirth = new JTextField(student.getDateOfBirth());
        JTextField txtGPA = new JTextField(String.valueOf(student.getGpa()));
        JComboBox<String> comboStatus = new JComboBox<>(
                new String[]{"ACTIVE", "INACTIVE", "GRADUATED"});
        comboStatus.setSelectedItem(student.getStatus());

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtFirstName);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtLastName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Carrera:"));
        formPanel.add(txtCareer);
        formPanel.add(new JLabel("Semestre:"));
        formPanel.add(txtSemester);
        formPanel.add(new JLabel("Fecha Nacimiento:"));
        formPanel.add(txtDateOfBirth);
        formPanel.add(new JLabel("GPA:"));
        formPanel.add(txtGPA);
        formPanel.add(new JLabel("Estado:"));
        formPanel.add(comboStatus);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");

        btnSave.addActionListener(e -> {
            try {
                boolean success = studentController.updateStudent(
                        studentId,
                        txtFirstName.getText(),
                        txtLastName.getText(),
                        txtEmail.getText(),
                        txtPhone.getText(),
                        txtCareer.getText(),
                        Integer.parseInt(txtSemester.getText()),
                        txtDateOfBirth.getText(),
                        Double.parseDouble(txtGPA.getText()),
                        comboStatus.getSelectedItem().toString()
                );

                if (success) {
                    loadStudents();
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

    private void deleteStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un estudiante para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean success = studentController.deleteStudent(studentId);

        if (success) {
            loadStudents();
        }
    }
}