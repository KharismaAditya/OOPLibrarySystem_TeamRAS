package ui.profileSection;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import ui.loginSection.LoginGUI;
import ui.programSection.dataBook.*;

import java.sql.*;

public class ProfileGUI extends Application {

    private static final String dbURL = "jdbc:mysql://127.0.0.1:3306/databook";
    private static final String dbUser  = "root";
    private static final String dbPass = "a2001234";

    private String idUser ;

    public ProfileGUI(String idUser ) {
        this.idUser  = idUser ;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Diri");
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color : #60B5FF");

        Label titleLabel = new Label("SELAMAT DATANG");
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setFont(Font.font("Impact", 28));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setMaxWidth(350);

        //Nama component
        Label namaLabel = new Label("Nama: ");
        namaLabel.setTextFill(Color.BLACK);
        Label namaValueLabel = new Label(getNama(idUser ));
        namaValueLabel.setTextFill(Color.BLACK);

        Label nimLabel = new Label("NIM: ");
        nimLabel.setTextFill(Color.BLACK);
        Label nimValueLabel = new Label(getNIM(idUser));
        nimValueLabel.setTextFill(Color.BLACK);

        grid.add(namaLabel, 0, 0);
        grid.add(namaValueLabel,1, 0 );
        grid.add(nimLabel, 0, 1);
        grid.add(nimValueLabel, 1, 1);


        //Button pinjam buku
        Button pinjamBukuButton = new Button("Pinjam Buku");
        pinjamBukuButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        pinjamBukuButton.setOnMouseEntered(e -> pinjamBukuButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        pinjamBukuButton.setOnMouseExited(e -> pinjamBukuButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        //Button kembalikan buku
        Button kembaliButton = new Button("Keluar");
        kembaliButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        kembaliButton.setOnMouseEntered(e -> kembaliButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        kembaliButton.setOnMouseExited(e -> kembaliButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        pinjamBukuButton.setOnAction(e -> {
            //displaybook display = new displaybook();
            //display.setMhsQuery(idUser);
           // display.start(new Stage());
            primaryStage.close();
        });

        kembaliButton.setOnAction(e -> {
            LoginGUI login = new LoginGUI();
            login.start(new Stage());
            primaryStage.close();
        });

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(pinjamBukuButton, kembaliButton);

        root.getChildren().addAll(titleLabel, grid, buttonBox);
        Scene scene = new Scene(root, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private String getNama(String idUser ) {
        String query = "SELECT nameUser FROM studentsdata WHERE idUser  = ?";
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
             PreparedStatement stms = conn.prepareStatement(query)) {

            stms.setString(1, idUser);
            ResultSet rs = stms.executeQuery();
            if (rs.next()) {
                return rs.getString("nameUser");
            } else {
                return "Nama tidak ditemukan";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String getNIM(String idUser){
        String query = "SELECT idUser FROM studentsdata WHERE idUser = ?";
        try(Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, idUser);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getString("idUser");
            }else{
                return "NIM tidak ditemukan";
            }
        }catch (SQLException e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}