package services;

import dao.SubjectDAO;
import model.Subject;
import model.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class SubjectService {
    private final SubjectDAO subjectDAO;

    public SubjectService() {
        Connection conn = DatabaseConnection.getConnection();
        this.subjectDAO = new SubjectDAO(conn);
    }

    public boolean addSubject(Subject subject) {
        if (!validateSubject(subject)) {
            return false;
        }

        if (subjectDAO.getByCode(subject.getCode()).isPresent()) {
            showError("El código de la materia ya está registrado");
            return false;
        }

        boolean success = subjectDAO.save(subject);
        if (success) {
            showSuccess("Materia agregada exitosamente");
        } else {
            showError("Error al agregar la materia");
        }
        return success;
    }

    public boolean updateSubject(Subject subject) {
        if (!validateSubject(subject)) {
            return false;
        }

        Optional<Subject> existingSubject = subjectDAO.getByCode(subject.getCode());
        if (existingSubject.isPresent() && existingSubject.get().getSubjectId() != subject.getSubjectId()) {
            showError("El código de la materia ya está en uso por otra materia");
            return false;
        }

        boolean success = subjectDAO.update(subject);
        if (success) {
            showSuccess("Materia actualizada exitosamente");
        } else {
            showError("Error al actualizar la materia");
        }
        return success;
    }

    public boolean deleteSubject(int subjectId) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar esta materia?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = subjectDAO.deleteById(subjectId);
            if (success) {
                showSuccess("Materia eliminada exitosamente");
            } else {
                showError("Error al eliminar la materia");
            }
            return success;
        }
        return false;
    }

    public List<Subject> getAllSubjects() {
        return subjectDAO.getAll();
    }

    public Optional<Subject> getSubjectById(int id) {
        return subjectDAO.get(id);
    }

    public Optional<Subject> getSubjectByCode(String code) {
        return subjectDAO.getByCode(code);
    }

    public List<Subject> getSubjectsByProfessor(int professorId) {
        return subjectDAO.getByProfessor(professorId);
    }

    public List<Subject> getSubjectsByDepartment(String department) {
        return subjectDAO.getByDepartment(department);
    }

    public List<Subject> getBasicSubjects() {
        return subjectDAO.getByDifficultyLevel("BASIC");
    }

    public List<Subject> getIntermediateSubjects() {
        return subjectDAO.getByDifficultyLevel("INTERMEDIATE");
    }

    public List<Subject> getAdvancedSubjects() {
        return subjectDAO.getByDifficultyLevel("ADVANCED");
    }

    public List<Subject> getSubjectsByDifficultyLevel(String difficultyLevel) {
        return subjectDAO.getByDifficultyLevel(difficultyLevel);
    }

    public int countSubjects() {
        return subjectDAO.count();
    }

    public int countSubjectsByDepartment(String department) {
        return getSubjectsByDepartment(department).size();
    }

    public int countSubjectsByProfessor(int professorId) {
        return getSubjectsByProfessor(professorId).size();
    }

    public List<String> getAllSubjectDepartments() {
        List<Subject> subjects = getAllSubjects();
        return subjects.stream()
                .map(Subject::getDepartment)
                .distinct()
                .sorted()
                .toList();
    }

    public List<String> getAllDifficultyLevels() {
        return List.of("BASIC", "INTERMEDIATE", "ADVANCED");
    }

    private boolean validateSubject(Subject subject) {
        if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            showError("El nombre de la materia es obligatorio");
            return false;
        }

        if (subject.getCode() == null || subject.getCode().trim().isEmpty()) {
            showError("El código de la materia es obligatorio");
            return false;
        }

        if (subject.getCredits() < 1 || subject.getCredits() > 10) {
            showError("Los créditos deben estar entre 1 y 10");
            return false;
        }

        if (subject.getDepartment() == null || subject.getDepartment().trim().isEmpty()) {
            showError("El departamento es obligatorio");
            return false;
        }

        if (subject.getDifficultyLevel() == null || subject.getDifficultyLevel().trim().isEmpty()) {
            showError("El nivel de dificultad es obligatorio");
            return false;
        }

        if (!getAllDifficultyLevels().contains(subject.getDifficultyLevel().toUpperCase())) {
            showError("El nivel de dificultad debe ser: BASIC, INTERMEDIATE o ADVANCED");
            return false;
        }

        if (subject.getHoursPerWeek() < 1 || subject.getHoursPerWeek() > 20) {
            showError("Las horas por semana deben estar entre 1 y 20");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}