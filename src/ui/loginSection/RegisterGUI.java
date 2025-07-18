package ui.loginSection;
import Properties.databaseConnect;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class RegisterGUI extends Application {
    databaseConnect db = new databaseConnect();

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Register");

        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);

        //REGISTER LABEL
        Label titleLabel = new Label("BUAT AKUN BARU");
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setFont(Font.font("Elephant", 25));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setMaxWidth(350);

        Label perpusLabel = new Label("---PERPUSTAKAAN UMM---");
        perpusLabel.setTextFill(Color.BLACK);
        perpusLabel.setFont(Font.font("Impact", 14));

        //NEW NIM component
        Label newNimLabel = new Label("Masukkan NIM: ");
        newNimLabel.setTextFill(Color.BLACK);newNimLabel.setFont(Font.font("Georgia", 12));
        TextField newNimField = new TextField();
        newNimField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8");

        //NEW NAME component
        Label newNameLabel = new Label("Masukkan NAMA: ");
        newNameLabel.setTextFill(Color.BLACK);newNameLabel.setFont(Font.font("Georgia", 12));
        TextField newNameField = new TextField();
        newNameField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8");

        //NEW EMAIL component
        Label newEmailLabel = new Label("Masukkan Email: ");
        newEmailLabel.setTextFill(Color.BLACK);newEmailLabel.setFont(Font.font("Georgia", 12));
        TextField newEmailField = new TextField();
        newEmailField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8");

        //NEW DEPARTMENT component
        Label newDepLabel = new Label("Masukkan Jurusan anda: ");
        newDepLabel.setTextFill(Color.BLACK);newDepLabel.setFont(Font.font("Georgia", 12));
        TextField newDepField = new TextField();
        newDepField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8");


        grid.add(newNimLabel, 0, 0);
        grid.add(newNimField, 1, 0);
        grid.add(newNameLabel, 0, 1);
        grid.add(newNameField, 1, 1);
        grid.add(newEmailLabel, 0, 2);
        grid.add(newEmailField, 1, 2);
        grid.add(newDepLabel, 0, 3);
        grid.add(newDepField, 1, 3);

        GridPane.setHgrow(newNimField, Priority.ALWAYS);
        GridPane.setHgrow(newEmailField, Priority.ALWAYS);
        GridPane.setHgrow(newEmailField, Priority.ALWAYS);
        GridPane.setHgrow(newDepField, Priority.ALWAYS);

        //ANNOUNCE
        Label announce = new Label();
        announce.setTextFill(Color.YELLOW);
        announce.setFont(Font.font("Segoe UI", 14));
        announce.setWrapText(true);
        announce.setMaxWidth(350);
        announce.setAlignment(Pos.CENTER);

        //add button
        Button addButton = new Button("ADD");
        addButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        //Back button
        Button bckButton = new Button("BACK");
        bckButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        bckButton.setOnMouseExited(e -> bckButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        bckButton.setOnMouseEntered(e -> bckButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: BLACK; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        GridPane gridButton = new GridPane();

        gridButton.setAlignment(Pos.CENTER);
        gridButton.setVgap(15);
        gridButton.setHgap(10);
        gridButton.setMaxWidth(350);

        gridButton.add(addButton, 0, 0);
        gridButton.add(bckButton, 1, 0);

        addButton.setOnAction(e-> {
            String NIM = newNimField.getText();
            String Nama = newNameField.getText();
            String email = newEmailField.getText();
            String department = newDepField.getText();

            if (NIM.isEmpty() || Nama.isEmpty() || email.isEmpty() || department.isEmpty()) {
                announce.setTextFill(Color.web("#f44336"));
                announce.setText("INFORMASI tidak boleh kosong");
            } else if (checkUser(NIM) || checkName(Nama)) {
                announce.setTextFill(Color.web("#f44336"));
                announce.setText("Beberapa data sudah terpakai");
            } else {
                addUser(NIM, Nama, email, department, announce);
            }
        });
        bckButton.setOnAction(e -> {
            LoginGUI login = new LoginGUI();
            login.start(new Stage());
            primaryStage.close();
        });

        Image image1 = new Image("register.png");
        ImageView bgIV = new ImageView(image1);
        bgIV.setFitWidth(450);
        bgIV.setFitHeight(450);
        bgIV.setPreserveRatio(true);
        bgIV.setOpacity(0.8);

        StackPane mainroot = new StackPane();

        root.getChildren().addAll(titleLabel, perpusLabel, grid, announce, gridButton);
        mainroot.getChildren().addAll(bgIV,root);

        Scene scene = new Scene(mainroot, 450, 450);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private boolean checkUser(String NIM){
        String query = "SELECT * FROM studentsdata WHERE idUser = ?";
        try(Connection conn = db.getConnection();
            PreparedStatement stms = conn.prepareStatement(query)){

            stms.setString(1, NIM);
            ResultSet rs = stms.executeQuery();
            return rs.next();
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkName(String nama){
        String query = "SELECT * FROM studentsdata WHERE nameUser = ?";
        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, nama);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private void addUser(String nim, String nama, String email, String department, Label announce){
        String query = "INSERT INTO studentsdata (idUser, nameUser, email, jurusan) VALUES (?, ?, ?, ?)";
        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, nim);
            stmt.setString(2, nama);
            stmt.setString(3, email);
            stmt.setString(4, department);

            int rowInserted = stmt.executeUpdate();

            if(rowInserted > 0){
                announce.setTextFill(Color.web("#2ecc71"));
                announce.setText("Profile added successfully");

            }
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }
}
