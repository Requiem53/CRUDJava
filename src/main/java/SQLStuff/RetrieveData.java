package SQLStuff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RetrieveData {
	    public static void main(String[] args) {
            try (Connection connection = MySQLConnection.getConnection();
                    Statement statement = connection.createStatement()) {
                    String selectQuery = "SELECT * FROM users";
                    ResultSet resultSet = statement.executeQuery(selectQuery);
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");

                        System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
}
