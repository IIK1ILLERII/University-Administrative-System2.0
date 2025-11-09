package model;

public class Subject {
    private int subjectId;
    private String subjectName;
    private String code;
    private int credits;
    private int professorId;
    private String professorName;
    private String department;
    private String difficultyLevel;
    private int hoursPerWeek;

    public Subject() {
        this.difficultyLevel = "BASIC";
        this.credits = 3;
        this.hoursPerWeek = 4;
    }

    public Subject(String subjectName, String code, int credits, String department) {
        this();
        this.subjectName = subjectName;
        this.code = code;
        this.credits = credits;
        this.department = department;
    }

    // Getters and Setters
    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }

    public String getProfessorName() { return professorName; }
    public void setProfessorName(String professorName) { this.professorName = professorName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public int getHoursPerWeek() { return hoursPerWeek; }
    public void setHoursPerWeek(int hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }

    public String getDifficultyColor() {
        switch (difficultyLevel.toUpperCase()) {
            case "BASIC": return "🟢";
            case "INTERMEDIATE": return "🟡";
            case "ADVANCED": return "🔴";
            default: return "⚪";
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d créditos)", code, subjectName, credits);
    }

    public String toDetailedString() {
        return String.format(
                "Materia ID: %d\nCódigo: %s\nNombre: %s\nCréditos: %d\nDepartamento: %s\nDificultad: %s\nHoras/Semana: %d\nProfesor: %s",
                subjectId, code, subjectName, credits, department, difficultyLevel, hoursPerWeek,
                professorName != null ? professorName : "No asignado"
        );
    }
}