package pl.edu.agh.iisg.to.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import pl.edu.agh.iisg.to.executor.QueryExecutor;

public class Student {
    private final int id;

    private final String firstName;

    private final String lastName;

    private final int indexNumber;

    Student(final int id, final String firstName, final String lastName, final int indexNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public static Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        String insertSql = "INSERT INTO student (first_name, last_name, index_number) VALUES (?, ?, ?);";
        Object[] args = {firstName, lastName, indexNumber};

        try {
            int id = QueryExecutor.createAndObtainId(insertSql, args);
            return Student.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static Optional<Student> findByIndexNumber(final int indexNumber) {
        String sql = "SELECT * FROM student WHERE index_number = (?)";
        return find(indexNumber, sql);
    }

    public static Optional<Student> findById(final int id) {
        String sql = "SELECT * FROM student WHERE id = (?)";
        return find(id, sql);
    }

    private static Optional<Student> find(int value, String sql) {
        Object[] args = {value};
        try {
            ResultSet rs = QueryExecutor.read(sql, args);
            return Optional.of(new Student(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getInt("index_number")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Map<Course, Float> createReport() {
        String findStudentGrades ="SELECT * FROM grade WHERE student_id = (?)";
        Object[] args = {id()};

        try{
            Map<Course, Float> grades = new HashMap<>();
            Map<Course, Integer> numberOfGrades = new HashMap<>();
            Map<Course, Float> resultMap = new HashMap<>();
            ResultSet set = QueryExecutor.read(findStudentGrades, args);

            while(set.next()) {
                Course course = Course.findById(set.getInt("course_id")).get();

                if (!grades.containsKey(course) && !numberOfGrades.containsKey(course)) {
                    grades.put(course, set.getFloat("grade"));
                    numberOfGrades.put(course, 1);
                } else {
                    float grade = grades.get(course);
                    int gradeNumber = numberOfGrades.get(course);

                    grades.replace(course, set.getFloat("grade") + grade);
                    numberOfGrades.replace(course, gradeNumber + 1);
                }
            }

            for(Course course : grades.keySet()){
                resultMap.put(course, grades.get(course)/numberOfGrades.get(course));
            }

            return resultMap;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }

    public int id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public int indexNumber() {
        return indexNumber;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String FIRST_NAME = "first_name";

        public static final String LAST_NAME = "last_name";

        public static final String INDEX_NUMBER = "index_number";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Student student = (Student) o;

        if (id != student.id)
            return false;
        if (indexNumber != student.indexNumber)
            return false;
        if (!firstName.equals(student.firstName))
            return false;
        return lastName.equals(student.lastName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + indexNumber;
        return result;
    }
}
