package com.example.csit228f2_2;

import SQLStuff.CRUDHandler;
import SQLStuff.MySQLConnection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.sql.*;

public class NotesView {

    @FXML
    private Text txtLoggedInAs;

    @FXML
    private TextField tfNoteTitle;

    @FXML
    private TextArea taNoteContent;

    @FXML
    private VBox vNotesField;

    @FXML
    private Text txtStatus;

    @FXML
    public void initialize() throws SQLException {
        txtLoggedInAs.setText("Logged in as " + HelloApplication.currUser);
        txtStatus.setOpacity(0);

        try (Connection connection = MySQLConnection.getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            String createTableQuery = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "username VARCHAR(50) NOT NULL,"
                    + "password VARCHAR(50) NOT NULL)";
            statement.addBatch(createTableQuery);


            String createNotesTableQuery = "CREATE TABLE IF NOT EXISTS notes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "noteTitle VARCHAR(500) NOT NULL," +
                    "noteContent VARCHAR(500) NOT NULL," +
                    "userID int," +
                    "FOREIGN KEY (userID) REFERENCES users(id))";
            statement.addBatch(createNotesTableQuery);

            statement.executeBatch();

            String gatherNotesQuery = "SELECT * FROM notes WHERE userID = " + HelloApplication.loggedInUserID;
            ResultSet allUserNotes = statement.executeQuery(gatherNotesQuery);

            while (allUserNotes.next()) {
                String noteTitle = allUserNotes.getString("noteTitle");
                String noteContent = allUserNotes.getString("noteContent");
                vNotesField.getChildren().add(0, newNote(noteTitle, noteContent, allUserNotes.getInt(1)));
                txtStatus.setOpacity(0);

            }

            connection.commit();
        }
    }

    @FXML
    public void createNote() throws SQLException {
        String noteTitle = tfNoteTitle.getText();
        String noteContent = taNoteContent.getText();

        try (Connection connection = MySQLConnection.getConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO notes (noteTitle, noteContent, userID) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, noteTitle);
            preparedStatement.setString(2, noteContent);
            preparedStatement.setInt(3, HelloApplication.loggedInUserID);

            preparedStatement.executeUpdate();

            ResultSet insertID = preparedStatement.getGeneratedKeys();
            insertID.next();

            int noteID = insertID.getInt(1);

            connection.commit();

            System.out.println("MAYBE KEY: " + noteID);

            vNotesField.getChildren().add(0, newNote(noteTitle, noteContent, noteID));
        }
    }

    public Group newNote(String noteTitle, String noteContent, int noteID) throws SQLException {
        Group note = new Group();
        HBox hBox = new HBox();
        VBox vBox = new VBox();

        note.setId("note" + noteID);

        note.getChildren().add(hBox);
        hBox.getChildren().add(vBox);

        vBox.setMaxWidth(1920.0);
        vBox.setMaxHeight(1080.0);
        vBox.setPrefWidth(450.0);

        Text noteTitleContainer = new Text();
        Text noteContentContainer = new Text();

        noteTitleContainer.managedProperty().bind(noteTitleContainer.visibleProperty());
        noteContentContainer.managedProperty().bind(noteContentContainer.visibleProperty());

        vBox.getChildren().add(noteTitleContainer);
        vBox.getChildren().add(noteContentContainer);

        noteTitleContainer.setStrokeType(StrokeType.OUTSIDE);
        noteTitleContainer.setStrokeWidth(0.0);
        noteTitleContainer.setText(noteTitle);
        noteTitleContainer.setWrappingWidth(400);
        noteTitleContainer.setFont(new Font("System Bold", 18.0));
        VBox.setMargin(noteTitleContainer, new Insets(0,0,10.0,0));

        noteContentContainer.setStrokeType(StrokeType.OUTSIDE);
        noteContentContainer.setStrokeWidth(0.0);
        noteContentContainer.setText(noteContent);
        noteContentContainer.setWrappingWidth(400);
        noteContentContainer.setFont(new Font(14.0));

        hBox.setPadding(new Insets(0,0,20.0,0));

        VBox buttonsContainer = new VBox();
        hBox.getChildren().add(buttonsContainer);

        Button btnEdit = new Button();
        Button btnDelete = new Button();

        buttonsContainer.getChildren().add(btnEdit);
        buttonsContainer.getChildren().add(btnDelete);

        btnEdit.setMinWidth(60.0);
        btnEdit.mnemonicParsingProperty().set(false);
        btnEdit.setText("Edit");
        btnEdit.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(btnEdit, new Insets(0,0,5.0,0));

        btnDelete.setMinWidth(60.0);
        btnDelete.mnemonicParsingProperty().set(false);
        btnDelete.setText("Delete");

        btnEdit.managedProperty().bind(btnEdit.visibleProperty());
        btnDelete.managedProperty().bind(btnDelete.visibleProperty());

        Button btnEditDone = new Button();
        Button btnEditCancel = new Button();

        buttonsContainer.getChildren().add(btnEditDone);
        buttonsContainer.getChildren().add(btnEditCancel);

        btnEditDone.setMinWidth(60.0);
        btnEditDone.mnemonicParsingProperty().set(false);
        btnEditDone.setText("Done");
        btnEditDone.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(btnEditDone, new Insets(0,0,5.0,0));

        btnEditCancel.setMinWidth(60.0);
        btnEditCancel.mnemonicParsingProperty().set(false);
        btnEditCancel.setText("Cancel");

        btnEditDone.managedProperty().bind(btnEditDone.visibleProperty());
        btnEditCancel.managedProperty().bind(btnEditCancel.visibleProperty());

        btnEditDone.setVisible(false);
        btnEditCancel.setVisible(false);

        TextField newTitle = new TextField();
        TextArea newContent = new TextArea();

        newTitle.managedProperty().bind(newTitle.visibleProperty());
        newContent.managedProperty().bind(newContent.visibleProperty());

        newTitle.setMaxWidth(400);
        newTitle.setMaxHeight(1080);
        newTitle.setPrefWidth(400);
        VBox.setMargin(newTitle, new Insets(0,0,10,0));

        newContent.setMaxWidth(400);
        newContent.setMaxHeight(105);
        newContent.setPrefWidth(400);
        newContent.setPrefHeight(105);

        newTitle.setText(noteTitle);
        newContent.setText(noteContent);

        newTitle.setVisible(false);
        newContent.setVisible(false);

        vBox.getChildren().add(newTitle);
        vBox.getChildren().add(newContent);

        btnEdit.setOnMousePressed(new EventHandler<MouseEvent>() {
            Boolean isEditing = false;
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("hey you");

                newTitle.setText(noteTitle);
                newContent.setText(noteContent);

                noteTitleContainer.setVisible(isEditing);
                noteContentContainer.setVisible(isEditing);
                btnEdit.setVisible(isEditing);
                btnDelete.setVisible(isEditing);

                newTitle.setVisible(!isEditing);
                newContent.setVisible(!isEditing);
                btnEditDone.setVisible(!isEditing);
                btnEditCancel.setVisible(!isEditing);
            }
        });

        btnEditDone.setOnMousePressed(new EventHandler<MouseEvent>() {
            Boolean isEditing = true;
            @Override
            public void handle(MouseEvent mouseEvent) {
                String inputTitle = newTitle.getText();
                String inputContent = newContent.getText();

                try (Connection connection = MySQLConnection.getConnection()){
                    connection.setAutoCommit(false);

                    PreparedStatement preparedStatement = connection.prepareStatement(
                      "UPDATE notes SET noteTitle = ?, noteContent = ? WHERE id = ?"
                    );
                    preparedStatement.setString(1, inputTitle);
                    preparedStatement.setString(2, inputContent);
                    preparedStatement.setInt(3, noteID);
                    preparedStatement.executeUpdate();
                    System.out.println("succ");

                    connection.commit();

                    txtStatus.setText("Successfully edited note");
                    txtStatus.setFill(Paint.valueOf("GREEN"));
                    txtStatus.setOpacity(1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("hey you");
                noteTitleContainer.setVisible(isEditing);
                noteContentContainer.setVisible(isEditing);
                btnEdit.setVisible(isEditing);
                btnDelete.setVisible(isEditing);

                noteTitleContainer.setText(inputTitle);
                noteContentContainer.setText(inputContent);

                newTitle.setVisible(!isEditing);
                newContent.setVisible(!isEditing);
                btnEditDone.setVisible(!isEditing);
                btnEditCancel.setVisible(!isEditing);
            }
        });

        btnEditCancel.setOnMousePressed(new EventHandler<MouseEvent>() {
            Boolean isEditing = true;
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("hey you");
                noteTitleContainer.setVisible(isEditing);
                noteContentContainer.setVisible(isEditing);
                btnEdit.setVisible(isEditing);
                btnDelete.setVisible(isEditing);

                newTitle.setVisible(!isEditing);
                newContent.setVisible(!isEditing);
                btnEditDone.setVisible(!isEditing);
                btnEditCancel.setVisible(!isEditing);
            }
        });

        btnDelete.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("show me that solvable problem");

                try (Connection connection = MySQLConnection.getConnection()){
                    connection.setAutoCommit(false);

                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "DELETE FROM notes WHERE id = ?"
                    );
                    preparedStatement.setInt(1, noteID);
                    preparedStatement.executeUpdate();

                    connection.commit();

                    txtStatus.setText("Successfully deleted note");
                    txtStatus.setFill(Paint.valueOf("GREEN"));
                    txtStatus.setOpacity(1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                vNotesField.getChildren().remove(note);
            }
        });

        txtStatus.setText("Successfully added note");
        txtStatus.setFill(Paint.valueOf("GREEN"));
        txtStatus.setOpacity(1);
        return note;
    }

    @FXML
    public void onLogout(){
        HelloApplication.tfUsername.clear();
        HelloApplication.pfPassword.clear();
        HelloApplication.actionTarget.setText("");
        HelloApplication.actionTarget.setOpacity(0);

        HelloApplication.stage.setScene(HelloApplication.scene);
        HelloApplication.stage.show();
    }
}
