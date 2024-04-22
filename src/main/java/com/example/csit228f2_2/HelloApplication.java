package com.example.csit228f2_2;

import SQLStuff.CRUDHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    public static List<User> users;
    public static int loggedInUserID = -1;
    public static void main(String[] args) {
        launch();
    }
    public static Stage stage;
    public static Scene scene;

    public static TextField tfUsername;
    public static PasswordField pfPassword;
    public static Text actionTarget;

    @Override
    public void start(Stage stage) throws Exception {
        CRUDHandler crudHandler = new CRUDHandler();

        HelloApplication.stage = stage;

        AnchorPane pnMain = new AnchorPane();
        GridPane grid = new GridPane();
        pnMain.getChildren().add(grid);
        grid.setAlignment(Pos.CENTER);
        Text sceneTitle = new Text("Welcome to CSIT228");
        sceneTitle.setStrokeType(StrokeType.CENTERED);
        sceneTitle.setStrokeWidth(100);
        sceneTitle.setFill(Paint.valueOf("#325622"));
        sceneTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 69));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label lblUsername = new Label("Username: ");
        lblUsername.setTextFill(Paint.valueOf("#c251d5"));
        lblUsername.setFont(Font.font(40));
        grid.add(lblUsername, 0, 1);

        TextField tfUsername = new TextField();
        HelloApplication.tfUsername = tfUsername;
        tfUsername.setFont(Font.font(35));
        grid.add(tfUsername, 1, 1);

        Label lblPassword = new Label("Password: ");
        lblPassword.setTextFill(Paint.valueOf("#c251d5"));
        lblPassword.setFont(Font.font(40));
        grid.add(lblPassword, 0, 2);

        PasswordField pfPassword = new PasswordField();
        HelloApplication.pfPassword = pfPassword;
        pfPassword.setFont(Font.font(35));
        grid.add(pfPassword, 1, 2);

        Button btnShow = new Button("<*>");
        HBox hbShow = new HBox();
        hbShow.getChildren().add(btnShow);
        hbShow.setAlignment(Pos.CENTER);
        hbShow.setMaxWidth(150);
        TextField tfPassword = new TextField();

        tfPassword.setFont(Font.font(35));
        grid.add(tfPassword, 1, 2);
        tfPassword.setVisible(false);
        grid.add(hbShow, 2, 2);



        btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent actionEvent) {
                tfPassword.setText(pfPassword.getText());
                tfPassword.setVisible(true);
                pfPassword.setVisible(false);
                grid.add(new Button("Hello"), 4,4);
            }
        });

        btnShow.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tfPassword.setVisible(false);
                pfPassword.setVisible(true);
            }
        });

        Button btnSignIn = new Button("Sign In");
        btnSignIn.setFont(Font.font(45));
        HBox hbSignIn = new HBox();
        hbSignIn.getChildren().add(btnSignIn);
        hbSignIn.setAlignment(Pos.CENTER);
        grid.add(hbSignIn, 0, 3, 2, 1);
        final Text actionTarget = new Text("Hi");
        HelloApplication.actionTarget = actionTarget;
        actionTarget.setFont(Font.font(30));
        grid.add(actionTarget, 1, 6);

        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font(45));
        HBox hbRegister = new HBox();
        hbRegister.getChildren().add(btnRegister);
        hbRegister.setAlignment(Pos.CENTER);
        grid.add(hbRegister, 1, 4, 5, 1);

        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("parinig naman ng rap mo");
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                //ADD USER TO DATABASE
                crudHandler.retrieveData();

                boolean nonUnique = false;

                for (User user : CRUDHandler.users) {
                    if (username.equals(user.username)) {
                        actionTarget.setText("Username already in use");
                        actionTarget.setOpacity(1);
                        nonUnique = true;
                    }
                }

                if(!nonUnique){
                    crudHandler.insertData(username,password);

                    crudHandler.retrieveData();
                    for (User user : CRUDHandler.users) {
                        if (username.equals(user.username)) {
                            loggedInUserID = user.id;
                        }
                    }

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
                    try {
                        Scene scene = new Scene(loader.load());
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        btnSignIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                //ADD USER TO DATABASE
                crudHandler.retrieveData();

                for (User user : CRUDHandler.users) {
                    if (username.equals(user.username) && password.equals(user.password)) {
                        crudHandler.retrieveData();
                        for (User usera : CRUDHandler.users) {
                            if (username.equals(usera.username)) {
                                loggedInUserID = usera.id;
                            }
                        }
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
                        try {
                            Scene scene = new Scene(loader.load());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }
                actionTarget.setText("Invalid username/password");
                actionTarget.setOpacity(1);
            }
        });

        EventHandler<KeyEvent> fieldChange = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent actionEvent) {
                actionTarget.setOpacity(0);
            }
        };
        tfUsername.setOnKeyTyped(fieldChange);
        pfPassword.setOnKeyTyped(fieldChange);


        Scene scene = new Scene(pnMain, 700, 560);
        HelloApplication.scene = scene;
        stage.setScene(scene);
        stage.show();
    }
}