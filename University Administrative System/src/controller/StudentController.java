package controller;

import model.Student;
import services.StudentService;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

public class StudentController {
    private final StudentService studentService;

    public StudentController() {
        this.studentService = new StudentService();
    }

    public boolean addStudent(String firstName, String lastName, String email,
                              String phone, String career, int semester,
                              String dateOfBirth, double gpa) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setCareer(career);
        student.setSemester(semester);
        student.setDateOfBirth(dateOfBirth);
        student.setGpa(gpa);

        return studentService.addStudent(student);
    }

    public boolean updateStudent(int studentId, String firstName, String lastName,
                                 String email, String phone, String career,
                                 int semester, String dateOfBirth, double gpa, String status) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setCareer(career);
        student.setSemester(semester);
        student.setDateOfBirth(dateOfBirth);
        student.setGpa(gpa);
        student.setStatus(status);

        return studentService.updateStudent(student);
    }

    public boolean deleteStudent(int studentId) {
        return studentService.deleteStudent(studentId);
    }

    public void loadStudentsToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<Student> students = studentService.getAllStudents();
        for (Student student : students) {
            model.addRow(new Object[]{
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getCareer(),
                    student.getSemester(),
                    student.getGpa(),
                    student.getEnrollmentDate(),
                    student.getStatus()
            });
        }
    }

    public Optional<Student> getStudentById(int id) {
        return studentService.getStudentById(id);
    }

    public List<Student> searchStudents(String criteria, String searchType) {
        switch (searchType.toUpperCase()) {
            case "NAME":
                return studentService.getStudentsByName(criteria);
            case "CAREER":
                return studentService.getStudentsByCareer(criteria);
            case "EMAIL":
                return studentService.getStudentByEmail(criteria)
                        .map(List::of)
                        .orElse(List.of());
            case "SEMESTER":
                try {
                    int semester = Integer.parseInt(criteria);
                    return studentService.getStudentsBySemester(semester);
                } catch (NumberFormatException e) {
                    return List.of();
                }
            default:
                return studentService.getAllStudents();
        }
    }

    public int getTotalStudents() {
        return studentService.countStudents();
    }

    public List<Student> getActiveStudents() {
        return studentService.getActiveStudents();
    }
}