package services;

import dao.EnrollmentDAO;
import model.Enrollment;
import model.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentService() {
        Connection conn = DatabaseConnection.getConnection();
        this.enrollmentDAO = new EnrollmentDAO(conn);
    }

    public boolean enrollStudent(int studentId, int subjectId, int professorId, String semester) {
        if (!validateEnrollment(studentId, subjectId, professorId, semester)) {
            return false;
        }

        // Verificar si el estudiante ya está inscrito en esta materia en el mismo semestre
        List<Enrollment> existingEnrollments = enrollmentDAO.getByStudent(studentId);
        boolean alreadyEnrolled = existingEnrollments.stream()
                .anyMatch(e -> e.getSubjectId() == subjectId && e.getSemester().equals(semester));

        if (alreadyEnrolled) {
            showError("El estudiante ya está inscrito en esta materia para el semestre " + semester);
            return false;
        }

        Enrollment enrollment = new Enrollment(studentId, subjectId, professorId, semester);
        boolean success = enrollmentDAO.save(enrollment);
        if (success) {
            showSuccess("Estudiante inscrito exitosamente en la materia");
        } else {
            showError("Error al inscribir al estudiante");
        }
        return success;
    }

    public boolean updateGrade(int enrollmentId, double grade) {
        if (grade < 0 || grade > 100) {
            showError("La calificación debe estar entre 0 y 100");
            return false;
        }

        boolean success = enrollmentDAO.updateGrade(enrollmentId, grade);
        if (success) {
            showSuccess("Calificación actualizada exitosamente");
        } else {
            showError("Error al actualizar la calificación");
        }
        return success;
    }

    public boolean dropEnrollment(int enrollmentId) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de dar de baja esta inscripción?\nEsta acción no se puede deshacer.",
                "Confirmar Baja",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = enrollmentDAO.dropEnrollment(enrollmentId);
            if (success) {
                showSuccess("Inscripción dada de baja exitosamente");
            } else {
                showError("Error al dar de baja la inscripción");
            }
            return success;
        }
        return false;
    }

    public boolean deleteEnrollment(int enrollmentId) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar esta inscripción?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = enrollmentDAO.deleteById(enrollmentId);
            if (success) {
                showSuccess("Inscripción eliminada exitosamente");
            } else {
                showError("Error al eliminar la inscripción");
            }
            return success;
        }
        return false;
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.getAll();
    }

    public Optional<Enrollment> getEnrollmentById(int id) {
        return enrollmentDAO.get(id);
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        return enrollmentDAO.getByStudent(studentId);
    }

    public List<Enrollment> getEnrollmentsBySubject(int subjectId) {
        return enrollmentDAO.getBySubject(subjectId);
    }

    public List<Enrollment> getEnrollmentsByProfessor(int professorId) {
        return enrollmentDAO.getByProfessor(professorId);
    }

    public List<Enrollment> getEnrollmentsBySemester(String semester) {
        return enrollmentDAO.getBySemester(semester);
    }

    public List<Enrollment> getActiveEnrollments() {
        return enrollmentDAO.getByStatus("ENROLLED");
    }

    public List<Enrollment> getPassedEnrollments() {
        return enrollmentDAO.getByStatus("PASSED");
    }

    public List<Enrollment> getFailedEnrollments() {
        return enrollmentDAO.getByStatus("FAILED");
    }

    public List<Enrollment> getDroppedEnrollments() {
        return enrollmentDAO.getByStatus("DROPPED");
    }

    public int countEnrollments() {
        return enrollmentDAO.count();
    }

    public int countActiveEnrollments() {
        return getActiveEnrollments().size();
    }

    public int countEnrollmentsByStudent(int studentId) {
        return getEnrollmentsByStudent(studentId).size();
    }

    public int countEnrollmentsBySubject(int subjectId) {
        return getEnrollmentsBySubject(subjectId).size();
    }

    public int countEnrollmentsBySemester(String semester) {
        return getEnrollmentsBySemester(semester).size();
    }

    public double getAverageGradeBySubject(int subjectId) {
        return enrollmentDAO.getAverageGradeBySubject(subjectId);
    }

    public int getEnrollmentCountBySubject(int subjectId) {
        return enrollmentDAO.getEnrollmentCountBySubject(subjectId);
    }

    public double getStudentGPA(int studentId) {
        List<Enrollment> enrollments = getEnrollmentsByStudent(studentId);
        List<Enrollment> gradedEnrollments = enrollments.stream()
                .filter(e -> e.getGrade() != null && e.isPassed())
                .toList();

        if (gradedEnrollments.isEmpty()) return 0.0;

        double total = 0.0;
        for (Enrollment enrollment : gradedEnrollments) {
            total += enrollment.getGrade();
        }
        return total / gradedEnrollments.size();
    }

    public List<String> getAllSemesters() {
        List<Enrollment> enrollments = getAllEnrollments();
        return enrollments.stream()
                .map(Enrollment::getSemester)
                .distinct()
                .sorted()
                .toList();
    }

    // VALIDACIONES
    private boolean validateEnrollment(int studentId, int subjectId, int professorId, String semester) {
        if (studentId <= 0) {
            showError("ID de estudiante inválido");
            return false;
        }

        if (subjectId <= 0) {
            showError("ID de materia inválido");
            return false;
        }

        if (professorId <= 0) {
            showError("ID de profesor inválido");
            return false;
        }

        if (semester == null || semester.trim().isEmpty()) {
            showError("El semestre es obligatorio");
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