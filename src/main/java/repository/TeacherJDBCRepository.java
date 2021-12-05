package repository;

import model.Teacher;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class CourseJDBCRepository communicates with the teacher table in the DB
 * Date: 5.12.2021
 */
public class TeacherJDBCRepository implements ICrudRepo<Teacher> {
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
    public Teacher findOne(Teacher obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT teacherId, firstName, lastName FROM teacher";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                if (obj.getTeacherId() == resultSet.getInt("teacherId")) {
                    return new Teacher(resultSet.getString("firstName"), resultSet.getString("lastName"),
                            resultSet.getInt("teacherId"));
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
    public Iterable<Teacher> findAll() throws IOException {

        openConnection();
        List<Teacher> teacherList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT teacherId, firstName, lastName FROM teacher";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getInt("teacherId"));
                teacherList.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teacherList;
    }

    /**
     * @param obj the object to save in the DB
     * @return null if object was saved; returns the object if it already exists
     */
    @Override
    public Teacher save(Teacher obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO teacher VALUES (?,?,?)")) {
            if (findOne(obj) == null) {
                preparedStatement.setInt(1, obj.getTeacherId());
                preparedStatement.setString(2, obj.getFirstName());
                preparedStatement.setString(3, obj.getLastName());
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
    public Teacher update(Teacher obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE teacher SET firstName = ?, lastName = ? WHERE teacherId = ?")) {
            if (findOne(obj) != null) {
                preparedStatement.setString(1, obj.getFirstName());
                preparedStatement.setString(2, obj.getLastName());
                preparedStatement.setInt(3, obj.getTeacherId());
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
    public Teacher delete(Teacher obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM teacher WHERE teacherId = ?")) {
            if (findOne(obj) != null) {
                preparedStatement.setInt(1, obj.getTeacherId());
                preparedStatement.executeUpdate();
                return obj;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
