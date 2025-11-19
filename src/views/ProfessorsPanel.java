package views;

import controller.ProfessorController;
import model.Professor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ProfessorsPanel extends JPanel {
    private JTable professorsTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> comboSearchType;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;
    private ProfessorController professorController;

    public ProfessorsPanel() {
        professorController = new ProfessorController();
        initComponents();
        loadProfessors();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        comboSearchType = new JComboBox<>(new String[]{"Nombre", "Departamento", "Email"});
        searchPanel.add(comboSearchType);

        btnSearch = new JButton("🔍 Buscar");
        btnSearch.addActionListener(e -> searchProfessors());
        searchPanel.add(btnSearch);

        topPanel.add(searchPanel, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("➕ Agregar");
        btnEdit = new JButton("✏️ Editar");
        btnDelete = new JButton("🗑️ Eliminar");
        btnRefresh = new JButton("🔄 Actualizar");

        btnAdd.addActionListener(e -> showAddProfessorDialog());
        btnEdit.addActionListener(e -> showEditProfessorDialog());
        btnDelete.addActionListener(e -> deleteProfessor());
        btnRefresh.addActionListener(e -> loadProfessors());

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        topPanel.add(actionPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Apellido");
        tableModel.addColumn("Email");
        tableModel.addColumn("Teléfono");
        tableModel.addColumn("Departamento");
        tableModel.addColumn("Especialidad");
        tableModel.addColumn("Salario");
        tableModel.addColumn("Estado");

        professorsTable = new JTable(tableModel);
        professorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(professorsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Profesores"));
        add(scrollPane, BorderLayout.CENTER);

        add(createStatsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));

        int total = professorController.getTotalProfessors();

        JLabel lblTotal = new JLabel("Total Profesores: " + total);
        JLabel lblDepartments = new JLabel("Departamentos: " + getDepartmentCount());

        statsPanel.add(lblTotal);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lblDepartments);

        return statsPanel;
    }

    private int getDepartmentCount() {
        return 5; // Placeholder
    }

    public void loadProfessors() {
        professorController.loadProfessorsToTable(professorsTable);
    }

    private void searchProfessors() {
        String criteria = txtSearch.getText().trim();
        String searchType = comboSearchType.getSelectedItem().toString();

        if (criteria.isEmpty()) {
            loadProfessors();
            return;
        }

        List<Professor> professors;
        switch (searchType) {
            case "Departamento":
                professors = professorController.getProfessorsByDepartment(criteria);
                break;
            case "Nombre":
            default:
                professors = professorController.getProfessorsByDepartment("");
        }

        tableModel.setRowCount(0);
        for (Professor professor : professors) {
            tableModel.addRow(new Object[]{
                    professor.getProfessorId(),
                    professor.getFirstName(),
                    professor.getLastName(),
                    professor.getEmail(),
                    professor.getPhone(),
                    professor.getDepartment(),
                    professor.getSpecialty(),
                    String.format("$%.2f", professor.getSalary()),
                    professor.getStatus()
            });
        }
    }

    private void showAddProfessorDialog() {
        JDialog dialog = new JDialog((Frame) null, "Agregar Nuevo Profesor", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtDepartment = new JTextField();
        JTextField txtSpecialty = new JTextField();
        JTextField txtSalary = new JTextField("0.0");

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtFirstName);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtLastName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Departamento:"));
        formPanel.add(txtDepartment);
        formPanel.add(new JLabel("Especialidad:"));
        formPanel.add(txtSpecialty);
        formPanel.add(new JLabel("Salario:"));
        formPanel.add(txtSalary);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");

        btnSave.addActionListener(e -> {
            try {
                boolean success = professorController.addProfessor(
                        txtFirstName.getText(),
                        txtLastName.getText(),
                        txtEmail.getText(),
                        txtPhone.getText(),
                        txtDepartment.getText(),
                        txtSpecialty.getText(),
                        Double.parseDouble(txtSalary.getText())
                );

                if (success) {
                    loadProfessors();
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error en el formato del salario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditProfessorDialog() {
        int selectedRow = professorsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un profesor para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int professorId = (int) tableModel.getValueAt(selectedRow, 0);
        Optional<Professor> professorOpt = professorController.getProfessorById(professorId);

        if (professorOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar los datos del profesor", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Professor professor = professorOpt.get();
        JDialog dialog = new JDialog((Frame) null, "Editar Profesor", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtFirstName = new JTextField(professor.getFirstName());
        JTextField txtLastName = new JTextField(professor.getLastName());
        JTextField txtEmail = new JTextField(professor.getEmail());
        JTextField txtPhone = new JTextField(professor.getPhone());
        JTextField txtDepartment = new JTextField(professor.getDepartment());
        JTextField txtSpecialty = new JTextField(professor.getSpecialty());
        JTextField txtSalary = new JTextField(String.valueOf(professor.getSalary()));
        JComboBox<String> comboStatus = new JComboBox<>(
                new String[]{"ACTIVE", "INACTIVE", "ON_LEAVE"});
        comboStatus.setSelectedItem(professor.getStatus());

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtFirstName);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtLastName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Departamento:"));
        formPanel.add(txtDepartment);
        formPanel.add(new JLabel("Especialidad:"));
        formPanel.add(txtSpecialty);
        formPanel.add(new JLabel("Salario:"));
        formPanel.add(txtSalary);
        formPanel.add(new JLabel("Estado:"));
        formPanel.add(comboStatus);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Guardar Cambios");
        JButton btnCancel = new JButton("Cancelar");

        btnSave.addActionListener(e -> {
            try {
                boolean success = professorController.updateProfessor(
                        professorId,
                        txtFirstName.getText(),
                        txtLastName.getText(),
                        txtEmail.getText(),
                        txtPhone.getText(),
                        txtDepartment.getText(),
                        txtSpecialty.getText(),
                        Double.parseDouble(txtSalary.getText()),
                        comboStatus.getSelectedItem().toString()
                );

                if (success) {
                    loadProfessors();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this,
                            "Profesor actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error en el formato del salario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteProfessor() {
        int selectedRow = professorsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un profesor para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int professorId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean success = professorController.deleteProfessor(professorId);

        if (success) {
            loadProfessors();
        }
    }
}