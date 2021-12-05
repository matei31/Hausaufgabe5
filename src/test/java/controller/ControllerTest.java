package controller;

import model.Course;
import model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.CourseJDBCRepository;
import repository.EnrolledJDBCRepository;
import repository.StudentJDBCRepository;
import repository.TeacherJDBCRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    CourseJDBCRepository courseJDBCRepository = new CourseJDBCRepository();
    StudentJDBCRepository studentJDBCRepository = new StudentJDBCRepository();
    TeacherJDBCRepository teacherJDBCRepository = new TeacherJDBCRepository();
    EnrolledJDBCRepository enrolledJDBCRepository = new EnrolledJDBCRepository();
    Controller controller = new Controller(courseJDBCRepository,studentJDBCRepository,teacherJDBCRepository,enrolledJDBCRepository);

    Course course = new Course(100,"Algebra",10,5,70);
    Student student1 = new Student("Viorel","Curecheriu",1,30);
    Student student2 = new Student("Dorel","Lob",2,20);
    Student student3 = new Student("Victor","Grigore",3,25);

    @BeforeEach
    void setUp() throws IOException {
        studentJDBCRepository.save(student1);
        studentJDBCRepository.save(student2);
        studentJDBCRepository.delete(student3);
        courseJDBCRepository.save(course);
        enrolledJDBCRepository.delete(student1.getStudentId(),course.getCourseId());
    }

    @Test
    void testFindAllStudents() throws IOException {
        assertEquals(controller.findAllStudents().size(),2);
    }

    @Test
    void testAddStudent() throws IOException {
        assertEquals(controller.findAllStudents().size(),2);
        controller.addStudent("Victor","Grigore",3,25);
        assertEquals(controller.findAllStudents().size(),3);
    }

    @Test
    void testDeleteStudent() throws IOException {
        assertEquals(controller.findAllStudents().size(),2);
        controller.addStudent("Victor","Grigore",3,25);
        assertEquals(controller.findAllStudents().size(),3);
        controller.deleteStudent(student3);
        assertEquals(controller.findAllStudents().size(),2);
    }

    @Test
    void testSortStudentsByCredits() throws IOException {
        assertEquals(controller.findAllStudents().size(),2);
        assertEquals(controller.sortStudentsByCredits().get(0),student2);
        assertEquals(controller.sortStudentsByCredits().get(1),student1);
        assertEquals(controller.findAllStudents().size(),2);
    }

    @Test
    void testFilterStudentsByCredits() throws IOException {
        assertEquals(controller.findAllStudents().size(),2);
        assertEquals(controller.filterStudentsByCredits().size(),1);
        assertEquals(controller.filterStudentsByCredits().get(0),student2);
        assertEquals(controller.findAllStudents().size(),2);
    }

    @Test
    void testRegister() throws IOException {
        assertEquals(controller.findAllCourses().size(),1);
        assertEquals(controller.findAllStudents().size(),2);
        assertTrue(controller.register(student1, course));
    }
}