package repository;

import model.Student;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Class CourseJDBCRepository communicates with the student table in the DB
 * Date: 5.12.2021
 */
public class StudentJDBCRepository implements ICrudRepo<Student> {

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
    public Student findOne(Student obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT studentId, firstName, lastName, totalCredits FROM student";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                if (obj.getStudentId() == resultSet.getInt("studentId")) {
                    return new Student(resultSet.getString("firstName"), resultSet.getString("lastName"),
                            resultSet.getInt("studentId"), resultSet.getInt("totalCredits"));
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
    public Iterable<Student> findAll() throws IOException {

        openConnection();
        ArrayList<Student> studentList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            String QUERY = "SELECT studentId, firstName, lastName, totalCredits FROM student";
            ResultSet resultSet = statement.executeQuery(QUERY);

            while (resultSet.next()) {
                Student student = new Student(resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getInt("studentId"), resultSet.getInt("totalCredits"));
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList;
    }

    /**
     * @param obj the object to save in the DB
     * @return null if object was saved; returns the object if it already exists
     */
    @Override
    public Student save(Student obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student VALUES (?,?,?,?)")) {
            if (findOne(obj) == null) {
                preparedStatement.setInt(1, obj.getStudentId());
                preparedStatement.setString(2, obj.getFirstName());
                preparedStatement.setString(3, obj.getLastName());
                preparedStatement.setInt(4, obj.getTotalCredits());
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
    public Student update(Student obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE student SET firstName = ?, lastName = ?, totalCredits = ? WHERE studentId = ?")) {
            if (findOne(obj) != null) {
                preparedStatement.setString(1, obj.getFirstName());
                preparedStatement.setString(2, obj.getLastName());
                preparedStatement.setInt(3, obj.getTotalCredits());
                preparedStatement.setInt(4, obj.getStudentId());
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
    public Student delete(Student obj) throws IOException {

        openConnection();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM student WHERE studentId = ?")) {
            if (findOne(obj) != null) {
                preparedStatement.setInt(1, obj.getStudentId());
                preparedStatement.executeUpdate();
                return obj;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}