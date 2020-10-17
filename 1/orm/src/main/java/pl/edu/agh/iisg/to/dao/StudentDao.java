package pl.edu.agh.iisg.to.dao;

import java.util.*;

import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;

import javax.persistence.PersistenceException;

public class StudentDao extends GenericDao<Student> {

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        try {
            save(new Student(firstName, lastName, indexNumber));
            return findByIndexNumber(indexNumber);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        try {
            Student student = currentSession().createQuery("SELECT s FROM Student s WHERE s.indexNumber = :indexNumber", Student.class)
                    .setParameter("indexNumber", indexNumber).getSingleResult();
            return Optional.of(student);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Map<Course, Float> createReport(final Student student) {
        Map<Course, Float> grades = new HashMap<>();
        Map<Course, Integer> numberOfGrades = new HashMap<>();
        Map<Course, Float> resultMap = new HashMap<>();
        try {
            for (Grade grade : student.gradeSet()) {
                Course course = grade.course();
                if (!grades.containsKey(course) && !numberOfGrades.containsKey(course)) {
                    grades.put(course, grade.grade());
                    numberOfGrades.put(course, 1);
                }
                else {
                    float rememberedGrade = grades.get(course);
                    int gradeNumber = numberOfGrades.get(course);

                    grades.replace(course, grade.grade() + rememberedGrade);
                    numberOfGrades.replace(course, gradeNumber + 1);
                }
            }

            for (Course course : grades.keySet()) {
                resultMap.put(course, grades.get(course) / numberOfGrades.get(course));
            }

            return resultMap;
        } catch (PersistenceException e){
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }
}
