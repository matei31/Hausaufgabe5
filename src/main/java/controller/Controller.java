package controller;

import model.Course;
import model.Student;
import model.Teacher;
import repository.CourseJDBCRepository;
import repository.EnrolledJDBCRepository;
import repository.StudentJDBCRepository;
import repository.TeacherJDBCRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class Controller receives input from UI and executes the functionalities by communicating with the repositories
 * Date: 5.12.2021
 */
public record Controller(CourseJDBCRepository crepo, StudentJDBCRepository srepo, TeacherJDBCRepository trepo, EnrolledJDBCRepository erepo) {

    /**
     * Function addCourse takes the attributes of a course as input and calls the Create function in the repository
     * @return true if the course was created successfully
     */
    public boolean addCourse(int courseId, String name, int teacherId, int credits, int maxEnrollment) throws IOException {

        Course c = new Course(courseId, name, teacherId, credits, maxEnrollment);
        this.crepo.save(c);
        return true;
    }

    /**
     * Function addStudent takes the attributes of a student as input and calls the Create function in the repository
     * @return true if the student was created successfully
     */
    public boolean addStudent(String firstName, String lastName, int studentId, int totalCredits) throws IOException {
        Student s = new Student(firstName, lastName, studentId, totalCredits);
        this.srepo.save(s);
        return true;
    }

    /**
     * Function addCourse takes the attributes of a course as input and calls the Create function in the repository
     * @return true if the course was created successfully
     */
    public boolean addTeacher(String firstName, String lastName, int teacherId) throws IOException {
        Teacher teacher = new Teacher(firstName, lastName, teacherId);
        this.trepo.save(teacher);
        return true;
    }

    /**
     * Function register enrolls a student to a course by creating references to both in the Enrolled table in the DB
     * @param student is the student we want to enroll
     * @param course is the course
     * @return true if student was registered successfully
     */
    public boolean register(Student student, Course course) throws IOException {
        Student foundStudent = this.srepo.findOne(student);
        Course foundCourse = this.crepo.findOne(course);
        this.erepo.save(foundStudent.getStudentId(), foundCourse.getCourseId());
        return true;
    }

    /**
     * @return all courses in a list
     */
    public ArrayList<Course> findAllCourses() throws IOException {
        return (ArrayList<Course>) crepo.findAll();
    }

    /**
     * @return all students in a list
     */
    public ArrayList<Student> findAllStudents() throws IOException {
        return (ArrayList<Student>) srepo.findAll();
    }

    /**
     * @return all teachers in a list
     */
    public ArrayList<Teacher> findAllTeachers() throws IOException {
        return (ArrayList<Teacher>) trepo.findAll();
    }

    /**
     * @param course the course to be deleted
     * @return true if the course was deleted successfully
     */
    public boolean deleteCourse(Course course) throws IOException {
        Course foundCourse = this.crepo.findOne(course);
        this.erepo.deleteAllStudentsFromCourse(foundCourse.getCourseId());
        this.crepo.delete(foundCourse);
        return true;
    }

    /**
     * @param student the course to be deleted
     * @return true if the student was deleted successfully
     */
    public boolean deleteStudent(Student student) throws IOException {
        Student foundStudent = this.srepo.delete(student);
        this.erepo.deleteAllCoursesFromStudent(foundStudent.getStudentId());
        this.srepo.delete(foundStudent);
        return true;
    }

    /**
     * @param teacher the course to be deleted
     * @return true if the teacher was deleted successfully
     */
    public boolean deleteTeacher(Teacher teacher) throws IOException {
        Teacher foundTeacher = this.trepo.findOne(teacher);
        ArrayList<Course> courses = (ArrayList<Course>) this.crepo.findAll();
        for (Course c : courses) {
            if (c.getTeacherId() == foundTeacher.getTeacherId()) {
                this.deleteCourse(c);
            }
        }
        this.trepo.delete(foundTeacher);
        return true;
    }

    /**
     * Function sortStudentsByCredits sorts the students in ascending order by their no of credits using lambda function
     * @return a sorted list
     */
    public ArrayList<Student> sortStudentsByCredits() throws IOException {
        ArrayList<Student> newlist = (ArrayList<Student>) srepo.findAll();
        return (ArrayList<Student>) newlist.stream()
                .sorted(Comparator.comparingInt(Student::getTotalCredits))
                .collect(Collectors.toList());
    }

    /**
     * Function sortCoursesByCredits sorts the courses in ascending order by their no of credits using lambda function
     * @return a sorted list
     */
    public ArrayList<Course> sortCoursesByCredits() throws IOException {
        ArrayList<Course> newlist = (ArrayList<Course>) crepo.findAll();
        return (ArrayList<Course>) newlist.stream()
                .sorted(Comparator.comparingInt(Course::getCredits))
                .collect(Collectors.toList());
    }

    /**
     * Function filterStudentsByCredits filters the students with less than 25 credits using lambda function
     * @return a filtered list
     */
    public ArrayList<Student> filterStudentsByCredits() throws IOException {
        Predicate<Student> byCredits = student -> student.getTotalCredits() < 25;
        ArrayList<Student> newlist = (ArrayList<Student>) srepo.findAll();
        return (ArrayList<Student>) newlist.stream().filter(byCredits).collect(Collectors.toList());
    }

    /**
     * Function filterCoursesByCredits filters the course with less than a given no of credits using lambda function
     * @return a filtered list
     */
    public ArrayList<Course> filterCoursesByCredit(int credits) throws IOException {
        Predicate<Course> byCredits = course -> course.getCredits() < credits;
        ArrayList<Course> newlist = (ArrayList<Course>) crepo.findAll();
        return (ArrayList<Course>) newlist.stream().filter(byCredits).collect(Collectors.toList());
    }
}