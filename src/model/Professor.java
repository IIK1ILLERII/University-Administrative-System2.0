package model;

public class Professor {
    private int professorId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String specialty;
    private double salary;
    private String status;

    public Professor() {
        this.status = "ACTIVE";
    }

    public Professor(String firstName, String lastName, String email, String department, String specialty) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.specialty = specialty;
    }

    // Getters and Setters
    public int getProfessorId() { return professorId; }
    public void setProfessorId(int professorId) { this.professorId = professorId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s", firstName, lastName, department);
    }

    public String toDetailedString() {
        return String.format(
                "Profesor ID: %d\nNombre: %s %s\nEmail: %s\nDepartamento: %s\nEspecialidad: %s\nSalario: $%.2f\nEstado: %s",
                professorId, firstName, lastName, email, department, specialty, salary, status
        );
    }
}