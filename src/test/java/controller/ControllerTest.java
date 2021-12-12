package controller;

import model.Course;
import model.Student;
import model.Teacher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.CourseJDBCRepository;
import repository.EnrolledJDBCRepository;
import repository.StudentJDBCRepository;
import repository.TeacherJDBCRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerTest {

    static CourseJDBCRepository courseJDBCRepository = new CourseJDBCRepository();
    static StudentJDBCRepository studentJDBCRepository = new StudentJDBCRepository();
    static TeacherJDBCRepository teacherJDBCRepository = new TeacherJDBCRepository();
    static EnrolledJDBCRepository enrolledJDBCRepository = new EnrolledJDBCRepository();
    Controller controller = new Controller(courseJDBCRepository, studentJDBCRepository, teacherJDBCRepository, enrolledJDBCRepository);

    static Course course1 = new Course(100, "Algebra", 10, 5, 70);
    static Course course2 = new Course(101, "MAP", 10, 4, 35);
    static Student student1 = new Student("Razvan", "Razvanescu", 1, 30);
    static Student student2 = new Student("George", "Pop", 2, 20);
    static Student student3 = new Student("Mihai", "Ion", 5, 25);
    static Teacher teacher1 = new Teacher("Andrei", "Ionescu", 10);
    static Teacher teacher2 = new Teacher("John", "Doe", 12);

    @BeforeEach
    void setUp() throws IOException {
        courseJDBCRepository.save(course1);
        courseJDBCRepository.delete(course2);
        studentJDBCRepository.save(student1);
        studentJDBCRepository.save(student2);
        studentJDBCRepository.delete(student3);
        teacherJDBCRepository.save(teacher1);
        teacherJDBCRepository.delete(teacher2);
        enrolledJDBCRepository.delete(student1.getStudentId(), course1.getCourseId());
    }

    @AfterAll
    static void cleanUp() throws IOException {
        courseJDBCRepository.delete(course1);
        courseJDBCRepository.delete(course2);
        studentJDBCRepository.delete(student1);
        studentJDBCRepository.delete(student2);
        studentJDBCRepository.delete(student3);
        teacherJDBCRepository.delete(teacher1);
        teacherJDBCRepository.delete(teacher2);
        enrolledJDBCRepository.delete(student1.getStudentId(), course1.getCourseId());
    }

    @Test
    void testRegister() throws IOException {
        assertEquals(controller.findAllCourses().size(), 1);
        assertEquals(controller.findAllStudents().size(), 2);
        assertTrue(controller.register(student1, course1));
    }

    @Test
    void testAddStudent() throws IOException {
        assertEquals(controller.findAllStudents().size(), 2);
        controller.addStudent("Mihai", "Ion", 5, 25);
        assertEquals(controller.findAllStudents().size(), 3);
    }

    @Test
    void testFindAllStudents() throws IOException {
        assertEquals(controller.findAllStudents().size(), 2);
        assertEquals(controller.findAllStudents().get(0), student1);
        assertEquals(controller.findAllStudents().get(1), student2);
    }

    @Test
    void testDeleteStudent() throws IOException {
        assertEquals(controller.findAllStudents().size(), 2);
        controller.addStudent("Mihai", "Ion", 5, 25);
        assertEquals(controller.findAllStudents().size(), 3);
        controller.deleteStudent(student3);
        assertEquals(controller.findAllStudents().size(), 2);
    }

    @Test
    void testSortStudentsByCredits() throws IOException {
        assertEquals(controller.findAllStudents().size(), 2);
        assertEquals(controller.sortStudentsByCredits().get(0), student2);
        assertEquals(controller.sortStudentsByCredits().get(1), student1);
        assertEquals(controller.findAllStudents().size(), 2);
    }

    @Test
    void testFilterStudentsByCredits() throws IOException {
        assertEquals(controller.findAllStudents().size(), 2);
        assertEquals(controller.filterStudentsByCredits().size(), 1);
        assertEquals(controller.filterStudentsByCredits().get(0), student2);
        assertEquals(controller.findAllStudents().size(), 2);
    }

    @Test
    void testAddCourse() throws IOException {
        assertEquals(controller.findAllCourses().size(), 1);
        controller.addCourse(101, "MAP", 10, 4, 35);
        assertEquals(controller.findAllCourses().size(), 2);
    }

    @Test
    void testFindAllCourses() throws IOException {
        assertEquals(controller.findAllCourses().size(), 1);
        assertEquals(controller.findAllCourses().get(0), course1);
    }

    @Test
    void testDeleteCourse() throws IOException {
        assertEquals(controller.findAllCourses().size(), 1);
        controller.addCourse(101, "MAP", 10, 4, 35);
        assertEquals(controller.findAllCourses().size(), 2);
        controller.deleteCourse(course2);
        assertEquals(controller.findAllCourses().size(), 1);
    }

    @Test
    void testSortCoursesByCredits() throws IOException {
        controller.addCourse(101, "MAP", 10, 4, 35);
        assertEquals(controller.findAllCourses().size(), 2);
        assertEquals(controller.sortCoursesByCredits().get(0), course2);
        assertEquals(controller.sortCoursesByCredits().get(1), course1);
        assertEquals(controller.findAllCourses().size(), 2);
    }

    @Test
    void testFilterCoursesByCredits() throws IOException {
        controller.addCourse(101, "MAP", 10, 4, 35);
        assertEquals(controller.findAllCourses().size(), 2);
        assertEquals(controller.filterCoursesByCredit(5).size(), 1);
        assertEquals(controller.filterCoursesByCredit(5).get(0), course2);
        assertEquals(controller.findAllCourses().size(), 2);
    }

    @Test
    void testAddTeacher() throws IOException {
        assertEquals(controller.findAllTeachers().size(), 1);
        controller.addTeacher("John", "Doe", 12);
        assertEquals(controller.findAllTeachers().size(), 2);
    }

    @Test
    void testFindAllTeachers() throws IOException {
        assertEquals(controller.findAllTeachers().size(), 1);
        assertEquals(controller.findAllTeachers().get(0), teacher1);
    }

    @Test
    void testDeleteTeacher() throws IOException {
        assertEquals(controller.findAllTeachers().size(), 1);
        controller.addTeacher("John", "Doe", 12);
        assertEquals(controller.findAllTeachers().size(), 2);
        controller.deleteTeacher(teacher2);
        assertEquals(controller.findAllTeachers().size(), 1);
    }
}