package ui.loginSection;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ui.profileSection.ProfileGUI;


import java.sql.*;



public class LoginGUI extends Application {

    private static final String dbURL = "jdbc:mysql://127.0.0.1:3306/databook";
    private static final String dbUser = "root";
    private static final String dbPass = "a2001234";


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color : #60B5FF");

        //WELCOME BACK LABEL
        Label titleLabel = new Label("Welcome Back");
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setFont(Font.font("Tahoma", 30));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setMaxWidth(350);

        //PERPUSTAKAAN Label
        Label perpusLabel = new Label("---PERPUSTAKAAN UMM---");
        perpusLabel.setTextFill(Color.BLACK);
        perpusLabel.setFont(Font.font("Impact", 14));

        //NIM component
        Label nimLabel = new Label("NIM: ");
        nimLabel.setTextFill(Color.BLACK);
        TextField nimField = new TextField();
        nimField.setPromptText("Enter your NIM");nimField.setStyle("-fx-background-color: #AFDDFF; -fx-background-radius: 5; -fx-padding: 8;");

        grid.add(nimLabel, 0, 0);
        grid.add(nimField,1, 0 );
        GridPane.setHgrow(nimField, Priority.ALWAYS);

        //ANNOUNCE LABEL
        Label announce = new Label();
        announce.setTextFill(Color.YELLOW);
        announce.setFont(Font.font("Segoe UI", 14));
        announce.setWrapText(true);
        announce.setMaxWidth(350);
        announce.setAlignment(Pos.CENTER);

        //login button
        Button logButton = new Button("LOGIN");
        logButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        logButton.setOnMouseExited(e -> logButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        logButton.setOnMouseEntered(e -> logButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        //Register button
        Button regButton = new Button("REGISTER");
        regButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        regButton.setOnMouseExited(e -> regButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        regButton.setOnMouseEntered(e -> regButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: BLACK; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        GridPane gridButton = new GridPane();

        gridButton.setAlignment(Pos.CENTER);
        gridButton.setVgap(15);
        gridButton.setHgap(10);
        gridButton.setMaxWidth(350);

        gridButton.add(logButton, 0, 0);
        gridButton.add(regButton, 1, 0);

        logButton.setOnAction(e -> {
            String NIM = nimField.getText();
            if(NIM.isEmpty()){
                announce.setTextFill(Color.RED);
                announce.setText("NIM cannot be Empty");
                return;
            }

            if(NIM.equalsIgnoreCase("Admin200")){
                primaryStage.close();
            }

            if(checkNIM(NIM)){
                announce.setTextFill(Color.web("#2ecc71"));
                announce.setText("Login successful!");
                ProfileGUI userLogin = new ProfileGUI(NIM);
                userLogin.start(new Stage());

                primaryStage.close();
            }else{
                announce.setTextFill(Color.web("#f44336"));
                announce.setText("Invalid username or password.");
            }


        });

        regButton.setOnAction(e -> {
            RegisterGUI reg = new RegisterGUI();
            reg.start(new Stage());
            primaryStage.close();
        });

        root.getChildren().addAll(titleLabel,perpusLabel,grid,announce,gridButton);
        Scene scene = new Scene(root,450,350);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private boolean checkNIM(String NIM){
        String query = "SELECT * FROM studentsdata WHERE idUser = ?";
        try(Connection conn = DriverManager.getConnection(dbURL,dbUser,dbPass);
            PreparedStatement stms = conn.prepareStatement(query)){

            stms.setString(1, NIM);
            ResultSet rs = stms.executeQuery();
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}