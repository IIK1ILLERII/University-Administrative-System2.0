package services;

import dao.StudentDAO;
import model.Student;
import model.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService() {
        Connection conn = DatabaseConnection.getConnection();
        this.studentDAO = new StudentDAO(conn);
    }

    public boolean addStudent(Student student) {
        // Validaciones de negocio
        if (!validateStudent(student)) {
            return false;
        }

        // Verificar duplicados por email
        if (studentDAO.getByEmail(student.getEmail()).isPresent()) {
            showError("El email ya está registrado en el sistema");
            return false;
        }

        // Intentar guardar el estudiante
        boolean success = studentDAO.save(student);
        if (success) {
            showSuccess("Estudiante agregado exitosamente");
        } else {
            showError("Error al agregar el estudiante");
        }
        return success;
    }

    public boolean updateStudent(Student student) {
        if (!validateStudent(student)) {
            return false;
        }

        boolean success = studentDAO.update(student);
        if (success) {
            showSuccess("Estudiante actualizado exitosamente");
        } else {
            showError("Error al actualizar el estudiante");
        }
        return success;
    }

    public boolean deleteStudent(int studentId) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar este estudiante?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = studentDAO.deleteById(studentId);
            if (success) {
                showSuccess("Estudiante eliminado exitosamente");
            } else {
                showError("Error al eliminar el estudiante");
            }
            return success;
        }
        return false;
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    public Optional<Student> getStudentById(int id) {
        return studentDAO.get(id);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentDAO.getByEmail(email);
    }

    public List<Student> getStudentsByName(String name) {
        return studentDAO.getByName(name);
    }

    public List<Student> getStudentsByCareer(String career) {
        return studentDAO.getByCareer(career);
    }

    public List<Student> getStudentsBySemester(int semester) {
        return studentDAO.getBySemester(semester);
    }

    public List<Student> getActiveStudents() {
        return studentDAO.getByStatus("ACTIVE");
    }

    public List<Student> getInactiveStudents() {
        return studentDAO.getByStatus("INACTIVE");
    }

    public List<Student> getGraduatedStudents() {
        return studentDAO.getByStatus("GRADUATED");
    }

    public boolean updateStudentStatus(int studentId, String status) {
        Optional<Student> studentOpt = studentDAO.get(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setStatus(status);
            return studentDAO.update(student);
        }
        return false;
    }

    public boolean updateStudentGPA(int studentId, double gpa) {
        if (gpa < 0 || gpa > 5.0) {
            showError("El GPA debe estar entre 0.0 y 5.0");
            return false;
        }

        boolean success = studentDAO.updateGPA(studentId, gpa);
        if (success) {
            showSuccess("GPA actualizado exitosamente");
        }
        return success;
    }

    public int countStudents() {
        return studentDAO.count();
    }

    public int countActiveStudents() {
        return getActiveStudents().size();
    }

    public int countStudentsByCareer(String career) {
        return getStudentsByCareer(career).size();
    }

    public double getAverageGPA() {
        List<Student> students = getActiveStudents();
        if (students.isEmpty()) return 0.0;

        double total = 0.0;
        int count = 0;
        for (Student student : students) {
            if (student.getGpa() > 0) {
                total += student.getGpa();
                count++;
            }
        }
        return count > 0 ? total / count : 0.0;
    }

    private boolean validateStudent(Student student) {
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            showError("El nombre es obligatorio");
            return false;
        }

        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            showError("El apellido es obligatorio");
            return false;
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            showError("El email es obligatorio");
            return false;
        }

        if (!isValidEmail(student.getEmail())) {
            showError("El formato del email no es válido");
            return false;
        }

        if (student.getCareer() == null || student.getCareer().trim().isEmpty()) {
            showError("La carrera es obligatoria");
            return false;
        }

        if (student.getSemester() < 1 || student.getSemester() > 12) {
            showError("El semestre debe estar entre 1 y 12");
            return false;
        }

        if (student.getGpa() < 0 || student.getGpa() > 5.0) {
            showError("El GPA debe estar entre 0.0 y 5.0");
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