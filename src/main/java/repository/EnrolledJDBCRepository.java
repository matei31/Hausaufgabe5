package repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Class CourseJDBCRepository communicates with the enrolled table in the DB
 * Date: 5.12.2021
 */
public class EnrolledJDBCRepository {

    private String DB_URL;
    private String USER;
    private String PASS;

    /**
     * Function openConnection reads the connection data from the properties file and opens the connection to the DB
     */
    public void openConnection() throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\user\\IdeaProjects\\Hausaufgabe5\\src\\main\\resources\\config.properties");
        Properties prop = new Properties();
        prop.load(fis);
        DB_URL = prop.getProperty("DB_URL");
        USER = prop.getProperty("USER");
        PASS = prop.getProperty("PASS");
    }

    /**
     * @return a list of courseIds associated with the given student
     */
    public ArrayList<Integer> findCoursesByStudentId(int studentId) throws IOException {
        openConnection();
        ArrayList<Integer> coursesIds = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT studentId, courseId FROM enrolled";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                if (resultSet.getInt("studentId") == studentId) {
                    coursesIds.add(resultSet.getInt("courseId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursesIds;
    }

    /**
     * @return a list of studentIds associated with the given course
     */
    public ArrayList<Integer> findStudentsByCourseId(int courseId) throws IOException {
        openConnection();
        ArrayList<Integer> studentsIds = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT studentId, courseId FROM enrolled";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                if (resultSet.getInt("courseId") == courseId) {
                    studentsIds.add(resultSet.getInt("studentId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentsIds;
    }

    /**
     * @return null if object was saved; returns the object if it already exists
     */
    public boolean save(int studentId, int courseId) throws IOException {
        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO enrolled VALUES (?,?)")) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @returntrue if the object was deleted; false otherwise
     */
    public boolean delete(int studentId, int courseId) throws IOException {
        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM enrolled WHERE studentId = ? AND courseId = ?")) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Function deleteAllStudentsFromCourse removes all the lines in the enrolled table that contain the given courseId
     * @param courseId is the course from which we want to delete the students
     * @return true if students were deleted; false otherwise
     */
    public boolean deleteAllStudentsFromCourse(int courseId) throws IOException {
        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM enrolled WHERE courseId = ?")) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Function deleteAllCoursesFromStudent removes all the lines in the enrolled table that contain the given studentId
     * @param studentId is the student from which we want to delete the courses
     * @return true if courses were deleted; false otherwise
     */
    public boolean deleteAllCoursesFromStudent(int studentId) throws IOException {
        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM enrolled WHERE studentId = ?")) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
