package model;

import java.time.LocalDate;

public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int subjectId;
    private int professorId;
    private String enrollmentDate;
    private Double grade;
    private String status;
    private String semester;
    private String studentName;
    private String subjectName;
    private String professorName;

    public Enrollment() {
        this.status = "ENROLLED";
        this.enrollmentDate = LocalDate.now().toString();
    }

    public Enrollment(int studentId, int subjectId, int professorId, String semester) {
        this();
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.professorId = professorId;
        this.semester = semester;
    }

    // Getters and Setters
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }

    public String getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getProfessorName() { return professorName; }
    public void setProfessorName(String professorName) { this.professorName = professorName; }

    public boolean isPassed() {
        return grade != null && grade >= 70.0;
    }

    public boolean isFailed() {
        return grade != null && grade < 70.0;
    }

    public boolean isEnrolled() {
        return "ENROLLED".equals(status);
    }

    public String getGradeStatus() {
        if (grade == null) return "En curso";
        return isPassed() ? "Aprobado" : "Reprobado";
    }

    public String getGradeColor() {
        if (grade == null) return "🟡";
        return isPassed() ? "🟢" : "🔴";
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) - %s",
                studentName, subjectName, semester, getGradeStatus());
    }
}