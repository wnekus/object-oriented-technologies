package pl.edu.agh.iisg.to.dao;

import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;

import javax.persistence.PersistenceException;

public class GradeDao extends GenericDao<Grade> {

    public boolean gradeStudent(final Student student, final Course course, final float grade) {
        try{
            Grade newGrade = new Grade(student, course, grade);
            save(newGrade);

            student.gradeSet().add(newGrade);
            course.gradeSet().add(newGrade);

            update(newGrade);

            return true;
        } catch (PersistenceException e){
            e.printStackTrace();
        }
        return false;
    }


}
