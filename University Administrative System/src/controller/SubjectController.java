package controller;

import model.Subject;
import services.SubjectService;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController() {
        this.subjectService = new SubjectService();
    }

    public boolean addSubject(String subjectName, String code, int credits,
                              int professorId, String department,
                              String difficultyLevel, int hoursPerWeek) {
        Subject subject = new Subject();
        subject.setSubjectName(subjectName);
        subject.setCode(code);
        subject.setCredits(credits);
        subject.setProfessorId(professorId);
        subject.setDepartment(department);
        subject.setDifficultyLevel(difficultyLevel);
        subject.setHoursPerWeek(hoursPerWeek);

        return subjectService.addSubject(subject);
    }

    public boolean updateSubject(int subjectId, String subjectName, String code,
                                 int credits, int professorId, String department,
                                 String difficultyLevel, int hoursPerWeek) {
        Subject subject = new Subject();
        subject.setSubjectId(subjectId);
        subject.setSubjectName(subjectName);
        subject.setCode(code);
        subject.setCredits(credits);
        subject.setProfessorId(professorId);
        subject.setDepartment(department);
        subject.setDifficultyLevel(difficultyLevel);
        subject.setHoursPerWeek(hoursPerWeek);

        return subjectService.updateSubject(subject);
    }

    public boolean deleteSubject(int subjectId) {
        return subjectService.deleteSubject(subjectId);
    }

    public void loadSubjectsToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<Subject> subjects = subjectService.getAllSubjects();
        for (Subject subject : subjects) {
            model.addRow(new Object[]{
                    subject.getSubjectId(),
                    subject.getCode(),
                    subject.getSubjectName(),
                    subject.getCredits(),
                    subject.getProfessorName(),
                    subject.getDepartment(),
                    subject.getDifficultyLevel(),
                    subject.getHoursPerWeek()
            });
        }
    }

    public Optional<Subject> getSubjectById(int id) {
        return subjectService.getSubjectById(id);
    }

    public List<Subject> getSubjectsByProfessor(int professorId) {
        return subjectService.getSubjectsByProfessor(professorId);
    }

    public List<Subject> getSubjectsByDepartment(String department) {
        return subjectService.getSubjectsByDepartment(department);
    }

    // MÉTODOS NUEVOS AGREGADOS
    public List<Subject> getBasicSubjects() {
        return subjectService.getBasicSubjects();
    }

    public List<Subject> getIntermediateSubjects() {
        return subjectService.getIntermediateSubjects();
    }

    public List<Subject> getAdvancedSubjects() {
        return subjectService.getAdvancedSubjects();
    }

    public int getTotalSubjects() {
        return subjectService.countSubjects();
    }
}