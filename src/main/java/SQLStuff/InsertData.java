package SQLStuff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertData {
    public static void main(String[] args) {
        try (Connection connection = MySQLConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
             "INSERT INTO users (name, email) VALUES (?, ?)")) {
                String name = "Mark Baring";
                String email = "feche@gmail.com";

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                        System.out.println("Data inserted successfully!");
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
