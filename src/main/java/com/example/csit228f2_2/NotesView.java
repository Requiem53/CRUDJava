package com.example.csit228f2_2;

import SQLStuff.CRUDHandler;
import SQLStuff.MySQLConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class NotesView {

    @FXML
    private Text txtLoggedInAs;

    @FXML
    private TextField tfNoteTitle;

    @FXML
    private TextArea taNoteContent;

    private CRUDHandler crudHandler;
    private Connection connection;


    @FXML
    public void initialize() throws SQLException {
        txtLoggedInAs.setText("Logged in as " + HelloApplication.currUser);
        connection = MySQLConnection.getConnection();
        crudHandler = new CRUDHandler();

        Statement statement = connection.createStatement();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS notes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "noteTitle VARCHAR(500) NOT NULL," +
                "noteContent VARCHAR(500) NOT NULL," +
                "userID int," +
                "FOREIGN KEY (userID) REFERENCES users(id))";
    }

    @FXML
    public void createNote() throws SQLException {
        String noteTitle = tfNoteTitle.getText();
        String noteContent = taNoteContent.getText();

        PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO notes (noteTitle, noteContent, userID) VALUES (?, ?, ?)"
        );
        
        preparedStatement.setString(1, noteTitle);
        preparedStatement.setString(2, noteContent);
        preparedStatement.setInt(3, HelloApplication.loggedInUserID);

        preparedStatement.executeUpdate();
    }
}
