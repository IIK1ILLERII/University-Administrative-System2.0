package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String career;
    private int semester;
    private String enrollmentDate;
    private String dateOfBirth;
    private String status;
    private double gpa;

    public Student() {
        this.status = "ACTIVE";
        this.gpa = 0.0;
        this.enrollmentDate = LocalDate.now().toString();
    }

    public Student(String firstName, String lastName, String email, String career, int semester) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.career = career;
        this.semester = semester;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCareer() { return career; }
    public void setCareer(String career) { this.career = career; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s (Semestre %d)", firstName, lastName, career, semester);
    }

    public String toDetailedString() {
        return String.format(
                "Estudiante ID: %d\nNombre: %s %s\nEmail: %s\nCarrera: %s\nSemestre: %d\nGPA: %.2f\nEstado: %s",
                studentId, firstName, lastName, email, career, semester, gpa, status
        );
    }
}