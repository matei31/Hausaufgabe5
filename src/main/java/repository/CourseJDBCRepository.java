package repository;

import model.Course;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class CourseJDBCRepository communicates with the course table in the DB
 * Date: 5.12.2021
 */
public class CourseJDBCRepository implements ICrudRepo<Course> {

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
     * @param obj the object to find
     * @return the object if it was found; null otherwise
     */
    @Override
    public Course findOne(Course obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT courseId, name, teacherId, credits, maxEnrollment FROM course";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                if (obj.getCourseId() == resultSet.getInt("courseId")) {
                    return new Course(resultSet.getInt("courseId"), resultSet.getString("name"), resultSet.getInt("teacherId"), resultSet.getInt("credits"), resultSet.getInt("maxEnrollment"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return all courses from the DB
     */
    @Override
    public Iterable<Course> findAll() throws IOException {

        openConnection();
        List<Course> courseList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT courseId, name, teacherId, credits, maxEnrollment FROM course";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                Course course = new Course(resultSet.getInt("courseId"), resultSet.getString("name"), resultSet.getInt("teacherId"), resultSet.getInt("credits"), resultSet.getInt("maxEnrollment"));
                courseList.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    /**
     * @param obj the object to save in the DB
     * @return null if object was saved; returns the object if it already exists
     */
    @Override
    public Course save(Course obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO course VALUES (?,?,?,?,?)")) {
            if (findOne(obj) == null) {
                preparedStatement.setInt(1, obj.getCourseId());
                preparedStatement.setString(2, obj.getName());
                preparedStatement.setInt(3, obj.getTeacherId());
                preparedStatement.setInt(4, obj.getCredits());
                preparedStatement.setInt(5, obj.getMaxEnrollment());
                preparedStatement.executeUpdate();
                return null;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @param obj is the object to be updated
     * @return null if the object was updated; the object itself if no changes were made to it
     */
    @Override
    public Course update(Course obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE course SET name = ?, teacherId = ?, credits = ?, maxEnrollment = ? WHERE courseId = ?")) {
            if (findOne(obj) != null) {
                preparedStatement.setString(1, obj.getName());
                preparedStatement.setInt(2, obj.getTeacherId());
                preparedStatement.setInt(3, obj.getCredits());
                preparedStatement.setInt(4, obj.getMaxEnrollment());
                preparedStatement.setInt(5, obj.getCourseId());
                preparedStatement.executeUpdate();
                return null;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * @param obj is the object to be deleted
     * @return the object if it was deleted; null otherwise
     */
    @Override
    public Course delete(Course obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM course WHERE courseId = ?")) {
            if (findOne(obj) != null) {
                preparedStatement.setInt(1, obj.getCourseId());
                preparedStatement.executeUpdate();
                return obj;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}