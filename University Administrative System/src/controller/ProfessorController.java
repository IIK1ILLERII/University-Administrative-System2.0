package controller;

import model.Professor;
import services.ProfessorService;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

public class ProfessorController {
    private final ProfessorService professorService;

    public ProfessorController() {
        this.professorService = new ProfessorService();
    }

    public boolean addProfessor(String firstName, String lastName, String email,
                                String phone, String department, String specialty,
                                double salary) {
        Professor professor = new Professor();
        professor.setFirstName(firstName);
        professor.setLastName(lastName);
        professor.setEmail(email);
        professor.setPhone(phone);
        professor.setDepartment(department);
        professor.setSpecialty(specialty);
        professor.setSalary(salary);

        return professorService.addProfessor(professor);
    }

    public boolean updateProfessor(int professorId, String firstName, String lastName,
                                   String email, String phone, String department,
                                   String specialty, double salary, String status) {
        Professor professor = new Professor();
        professor.setProfessorId(professorId);
        professor.setFirstName(firstName);
        professor.setLastName(lastName);
        professor.setEmail(email);
        professor.setPhone(phone);
        professor.setDepartment(department);
        professor.setSpecialty(specialty);
        professor.setSalary(salary);
        professor.setStatus(status);

        return professorService.updateProfessor(professor);
    }

    public boolean deleteProfessor(int professorId) {
        return professorService.deleteProfessor(professorId);
    }

    public void loadProfessorsToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<Professor> professors = professorService.getAllProfessors();
        for (Professor professor : professors) {
            model.addRow(new Object[]{
                    professor.getProfessorId(),
                    professor.getFirstName(),
                    professor.getLastName(),
                    professor.getEmail(),
                    professor.getPhone(),
                    professor.getDepartment(),
                    professor.getSpecialty(),
                    professor.getSalary(),
                    professor.getStatus()
            });
        }
    }

    public Optional<Professor> getProfessorById(int id) {
        return professorService.getProfessorById(id);
    }

    public List<Professor> getProfessorsByDepartment(String department) {
        return professorService.getProfessorsByDepartment(department);
    }

    public int getTotalProfessors() {
        return professorService.countProfessors();
    }
}