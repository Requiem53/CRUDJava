package SQLStuff;

import com.example.csit228f2_2.User;

import java.net.MalformedURLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDHandler {
    public static final String URL = "jdbc:mysql://localhost:3306/javadb";
    public static final String USERNAME = "requiem53";
    public static final String PASSWORD = "requiem53";

    public static List<User> users;

    public static Connection getConnection(){
        Connection c = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database!");
        }catch (ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }

        return c;
    }

    public CRUDHandler(){
        users = new ArrayList<>();
        getConnection();
        createTable();
    }

    public void createTable(){
        try (Connection connection = MySQLConnection.getConnection();
                Statement statement = connection.createStatement()) {
                String createTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "username VARCHAR(50) NOT NULL,"
                        + "password VARCHAR(50) NOT NULL)";
                statement.execute(createTableQuery);
                System.out.println("Table created successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void insertData(String username, String password){
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (?, ?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveData(){
        try (Connection connection = MySQLConnection.getConnection();
             Statement statement = connection.createStatement()) {
            users.clear();
            String selectQuery = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                System.out.println("ID: " + id + ", Username: " + username + ", Password: " + password);
                users.add(new User(id, username, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateData(int userIdToUpdate, String username, String password){
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET username = ?, password = ? WHERE id = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, userIdToUpdate);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUsername(int userIdToUpdate, String username){
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET username = ? WHERE id = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, userIdToUpdate);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(int userIdToUpdate, String password){
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET password = ? WHERE id = ?")) {

            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, userIdToUpdate);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(int userIdToDelete){
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM users WHERE id = ?")) {

            preparedStatement.setInt(1, userIdToDelete);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
