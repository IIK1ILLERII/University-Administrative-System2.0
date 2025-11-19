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

    private JLabel lblTotal, lblActive, lblInactive;
    private JPanel statsPanel;

    public StudentsPanel() {
        studentController = new StudentController();
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        comboSearchType = new JComboBox<>(new String[]{"Nombre", "Carrera", "Email", "Semestre"});
        searchPanel.add(comboSearchType);

        btnSearch = new JButton("Buscar");
        btnSearch.addActionListener(e -> searchStudents());
        searchPanel.add(btnSearch);

        topPanel.add(searchPanel, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Agregar");
        btnEdit = new JButton("Editar");
        btnDelete = new JButton("Eliminar");
        btnRefresh = new JButton("Actualizar");

        btnAdd.addActionListener(e -> showAddStudentDialog());
        btnEdit.addActionListener(e -> showEditStudentDialog());
        btnDelete.addActionListener(e -> deleteStudent());

        btnRefresh.addActionListener(e -> {
            refreshAllData();
        });

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        topPanel.add(actionPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);


        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        studentsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        studentsTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        studentsTable.getColumnModel().getColumn(7).setPreferredWidth(60);
        studentsTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(9).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Estudiantes"));
        add(scrollPane, BorderLayout.CENTER);

        statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas en Tiempo Real"));

        lblTotal = new JLabel("Total: 0");
        lblActive = new JLabel("Activos: 0");
        lblInactive = new JLabel("Inactivos: 0");

        lblTotal.setFont(new Font("Arial", Font.BOLD, 12));
        lblActive.setFont(new Font("Arial", Font.BOLD, 12));
        lblInactive.setFont(new Font("Arial", Font.BOLD, 12));

        lblTotal.setForeground(Color.BLUE);
        lblActive.setForeground(new Color(0, 128, 0)); // Verde
        lblInactive.setForeground(Color.RED);

        statsPanel.add(lblTotal);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblActive);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblInactive);

        return statsPanel;
    }

    public void loadStudents() {
        try {
            studentController.loadStudentsToTable(studentsTable);

            updateStats();

            tableModel.fireTableDataChanged();
            studentsTable.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar estudiantes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStats() {
        try {
            int total = studentController.getTotalStudents();
            int active = studentController.getActiveStudents().size();
            int inactive = total - active;

            lblTotal.setText("Total: " + total);
            lblActive.setText("Activos: " + active);
            lblInactive.setText("Inactivos: " + inactive);

            lblTotal.setForeground(total > 0 ? Color.BLUE : Color.GRAY);
            lblActive.setForeground(active > 0 ? new Color(0, 128, 0) : Color.GRAY);
            lblInactive.setForeground(inactive > 0 ? Color.RED : Color.GRAY);

            statsPanel.revalidate();
            statsPanel.repaint();

        } catch (Exception e) {
            System.err.println("Error actualizando estadísticas: " + e.getMessage());
        }
    }

    private void refreshAllData() {
        btnRefresh.setText("Actualizando...");
        btnRefresh.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(500);
                return null;
            }

            @Override
            protected void done() {
                try {
                    loadStudents();

                    btnRefresh.setText("Actualizar");
                    btnRefresh.setEnabled(true);

                    JOptionPane.showMessageDialog(StudentsPanel.this,
                            "Datos actualizados en tiempo real\n" +
                                    "• Tabla de estudiantes refrescada\n" +
                                    "• Estadísticas actualizadas\n" +
                                    "• Búsqueda disponible con datos actuales",
                            "Actualización Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(StudentsPanel.this,
                            "Error al actualizar: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);

                    btnRefresh.setText("Actualizar");
                    btnRefresh.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void searchStudents() {
        String criteria = txtSearch.getText().trim();
        String searchType = comboSearchType.getSelectedItem().toString();

        if (criteria.isEmpty()) {
            loadStudents();
            return;
        }

        try {
            String searchTypeEnglish;
            switch (searchType) {
                case "Nombre": searchTypeEnglish = "NAME"; break;
                case "Carrera": searchTypeEnglish = "CAREER"; break;
                case "Email": searchTypeEnglish = "EMAIL"; break;
                case "Semestre": searchTypeEnglish = "SEMESTER"; break;
                default: searchTypeEnglish = "NAME";
            }

            List<Student> students = studentController.searchStudents(criteria, searchTypeEnglish);
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

            updateStats();

            JOptionPane.showMessageDialog(this,
                    "Se encontraron " + students.size() + " estudiantes",
                    "Resultados de Búsqueda",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error en búsqueda: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                    refreshAllData();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(StudentsPanel.this,
                            "Estudiante agregado exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Error al agregar estudiante",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error en los datos numéricos. Verifique semestre y GPA.",
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error: " + ex.getMessage(),
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
                    refreshAllData();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(StudentsPanel.this,
                            "Estudiante actualizado exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Error al actualizar estudiante",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error en los datos numéricos. Verifique semestre y GPA.",
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error: " + ex.getMessage(),
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

    private void deleteStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un estudiante para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String studentName = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al estudiante?\n" +
                        "Nombre: " + studentName + "\n" +
                        "ID: " + studentId,
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = studentController.deleteStudent(studentId);

            if (success) {
                refreshAllData();
                JOptionPane.showMessageDialog(this,
                        "Estudiante eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar estudiante",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}