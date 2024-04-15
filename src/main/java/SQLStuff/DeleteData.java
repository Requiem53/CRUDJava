package SQLStuff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteData {
    public static void main(String[] args) {
        try (Connection connection = MySQLConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                             "DELETE FROM users WHERE id = ?")) {

                int userIdToDelete = 1;

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
