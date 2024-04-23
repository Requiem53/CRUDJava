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

    @FXML
    private VBox vNotesField;

    @FXML
    private Text txtStatus;

    private CRUDHandler crudHandler;
    private Connection connection;
    private int noteCounter = 0;


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
        System.out.println(HelloApplication.loggedInUserID);

        preparedStatement.executeUpdate();

        vNotesField.getChildren().add(0, newNote(noteTitle, noteContent));
    }

    public Group newNote(String noteTitle, String noteContent){
        Group note = new Group();
        HBox hBox = new HBox();
        VBox vBox = new VBox();

        note.getChildren().add(hBox);
        hBox.getChildren().add(vBox);

        vBox.setMaxWidth(1920.0);
        vBox.setMaxHeight(1080.0);
        vBox.setPrefWidth(450.0);

        Text noteTitleContainer = new Text();
        Text noteContentContainer = new Text();

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
        Button btnEdit = new Button();
        Button btnDelete = new Button();

        hBox.getChildren().add(buttonsContainer);
        buttonsContainer.getChildren().add(btnEdit);
        buttonsContainer.getChildren().add(btnDelete);

        btnEdit.setMinWidth(60.0);
        btnEdit.mnemonicParsingProperty().set(false);
        btnEdit.setText("Edit");
        btnEdit.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(btnEdit, new Insets(0,0,5.0,0));

        btnDelete.setMinWidth(60.0);
        btnEdit.mnemonicParsingProperty().set(false);
        btnDelete.setText("Delete");

        btnEdit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("hey you");
            }
        });

        btnDelete.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("show me that solvable problem");
            }
        });

        txtStatus.setText("Successfully added note");
        txtStatus.setFill(Paint.valueOf("GREEN"));
        txtStatus.setOpacity(1);
        noteCounter++;
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
