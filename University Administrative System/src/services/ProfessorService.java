package services;

import dao.ProfessorDAO;
import model.Professor;
import model.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class ProfessorService {
    private final ProfessorDAO professorDAO;

    public ProfessorService() {
        Connection conn = DatabaseConnection.getConnection();
        this.professorDAO = new ProfessorDAO(conn);
    }

    public boolean addProfessor(Professor professor) {
        if (!validateProfessor(professor)) {
            return false;
        }

        if (professorDAO.getByEmail(professor.getEmail()).isPresent()) {
            showError("El email ya está registrado en el sistema");
            return false;
        }

        boolean success = professorDAO.save(professor);
        if (success) {
            showSuccess("Profesor agregado exitosamente");
        } else {
            showError("Error al agregar el profesor");
        }
        return success;
    }

    public boolean updateProfessor(Professor professor) {
        if (!validateProfessor(professor)) {
            return false;
        }

        boolean success = professorDAO.update(professor);
        if (success) {
            showSuccess("Profesor actualizado exitosamente");
        } else {
            showError("Error al actualizar el profesor");
        }
        return success;
    }

    public boolean deleteProfessor(int professorId) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar este profesor?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = professorDAO.deleteById(professorId);
            if (success) {
                showSuccess("Profesor eliminado exitosamente");
            } else {
                showError("Error al eliminar el profesor");
            }
            return success;
        }
        return false;
    }

    public List<Professor> getAllProfessors() {
        return professorDAO.getAll();
    }

    public Optional<Professor> getProfessorById(int id) {
        return professorDAO.get(id);
    }

    public Optional<Professor> getProfessorByEmail(String email) {
        return professorDAO.getByEmail(email);
    }

    public List<Professor> getProfessorsByDepartment(String department) {
        return professorDAO.getByDepartment(department);
    }

    public List<Professor> getActiveProfessors() {
        return professorDAO.getByStatus("ACTIVE");
    }

    public List<Professor> getInactiveProfessors() {
        return professorDAO.getByStatus("INACTIVE");
    }

    public List<Professor> getProfessorsOnLeave() {
        return professorDAO.getByStatus("ON_LEAVE");
    }

    public boolean updateProfessorStatus(int professorId, String status) {
        Optional<Professor> professorOpt = professorDAO.get(professorId);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            professor.setStatus(status);
            return professorDAO.update(professor);
        }
        return false;
    }

    public boolean updateProfessorSalary(int professorId, double salary) {
        if (salary < 0) {
            showError("El salario no puede ser negativo");
            return false;
        }

        Optional<Professor> professorOpt = professorDAO.get(professorId);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            professor.setSalary(salary);
            return professorDAO.update(professor);
        }
        return false;
    }

    public int countProfessors() {
        return professorDAO.count();
    }

    public int countActiveProfessors() {
        return getActiveProfessors().size();
    }

    public int countProfessorsByDepartment(String department) {
        return getProfessorsByDepartment(department).size();
    }

    public double getAverageSalary() {
        List<Professor> professors = getActiveProfessors();
        if (professors.isEmpty()) return 0.0;

        double total = 0.0;
        for (Professor professor : professors) {
            total += professor.getSalary();
        }
        return total / professors.size();
    }

    public List<String> getAllDepartments() {
        List<Professor> professors = getAllProfessors();
        return professors.stream()
                .map(Professor::getDepartment)
                .distinct()
                .sorted()
                .toList();
    }

    // VALIDACIONES
    private boolean validateProfessor(Professor professor) {
        if (professor.getFirstName() == null || professor.getFirstName().trim().isEmpty()) {
            showError("El nombre es obligatorio");
            return false;
        }

        if (professor.getLastName() == null || professor.getLastName().trim().isEmpty()) {
            showError("El apellido es obligatorio");
            return false;
        }

        if (professor.getEmail() == null || professor.getEmail().trim().isEmpty()) {
            showError("El email es obligatorio");
            return false;
        }

        if (!isValidEmail(professor.getEmail())) {
            showError("El formato del email no es válido");
            return false;
        }

        if (professor.getDepartment() == null || professor.getDepartment().trim().isEmpty()) {
            showError("El departamento es obligatorio");
            return false;
        }

        if (professor.getSpecialty() == null || professor.getSpecialty().trim().isEmpty()) {
            showError("La especialidad es obligatoria");
            return false;
        }

        if (professor.getSalary() < 0) {
            showError("El salario no puede ser negativo");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}