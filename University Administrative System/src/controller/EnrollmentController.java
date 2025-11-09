package controller;

import model.Enrollment;
import services.EnrollmentService;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController() {
        this.enrollmentService = new EnrollmentService();
    }

    // MÉTODOS PARA LAS OPERACIONES BÁSICAS
    public boolean enrollStudent(int studentId, int subjectId, int professorId, String semester) {
        return enrollmentService.enrollStudent(studentId, subjectId, professorId, semester);
    }

    public boolean updateGrade(int enrollmentId, double grade) {
        return enrollmentService.updateGrade(enrollmentId, grade);
    }

    public boolean dropEnrollment(int enrollmentId) {
        return enrollmentService.dropEnrollment(enrollmentId);
    }

    public void loadEnrollmentsToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
            model.addRow(new Object[]{
                    enrollment.getEnrollmentId(),
                    enrollment.getStudentName(),
                    enrollment.getSubjectName(),
                    enrollment.getProfessorName(),
                    enrollment.getEnrollmentDate(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "N/A",
                    enrollment.getStatus(),
                    enrollment.getSemester()
            });
        }
    }

    // MÉTODOS PARA LAS ESTADÍSTICAS
    public int getTotalEnrollments() {
        return enrollmentService.countEnrollments();
    }

    public List<Enrollment> getActiveEnrollments() {
        return enrollmentService.getActiveEnrollments();
    }

    public List<Enrollment> getPassedEnrollments() {
        return enrollmentService.getPassedEnrollments();
    }

    // MÉTODOS ADICIONALES PARA CONSULTAS
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    public List<Enrollment> getEnrollmentsBySubject(int subjectId) {
        return enrollmentService.getEnrollmentsBySubject(subjectId);
    }

    public List<Enrollment> getEnrollmentsBySemester(String semester) {
        return enrollmentService.getEnrollmentsBySemester(semester);
    }

    public List<Enrollment> getEnrollmentsByProfessor(int professorId) {
        return enrollmentService.getEnrollmentsByProfessor(professorId);
    }

    public double getAverageGradeBySubject(int subjectId) {
        return enrollmentService.getAverageGradeBySubject(subjectId);
    }

    public int countActiveEnrollments() {
        return enrollmentService.countActiveEnrollments();
    }

    public int countEnrollmentsByStudent(int studentId) {
        return enrollmentService.countEnrollmentsByStudent(studentId);
    }

    public int countEnrollmentsBySubject(int subjectId) {
        return enrollmentService.countEnrollmentsBySubject(subjectId);
    }

    public double getStudentGPA(int studentId) {
        return enrollmentService.getStudentGPA(studentId);
    }

    public List<String> getAllSemesters() {
        return enrollmentService.getAllSemesters();
    }

    // MÉTODOS PARA OBTENER ENROLLMENTS POR ESTADO
    public List<Enrollment> getFailedEnrollments() {
        return enrollmentService.getFailedEnrollments();
    }

    public List<Enrollment> getDroppedEnrollments() {
        return enrollmentService.getDroppedEnrollments();
    }

    public Optional<Enrollment> getEnrollmentById(int id) {
        return enrollmentService.getEnrollmentById(id);
    }
}