package ui.adminSection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ui.loginSection.LoginGUI;
import ui.profileSection.ProfileGUI;
import ui.programSection.dataBook.Book;


import java.sql.*;


public class adminClassManage extends Application {
    private static final String dbURL = "jdbc:mysql://127.0.0.1:3306/databook";
    private static final String dbUser = "root";
    private static final String dbPass = "a2001234";

    private ObservableList<students> masterData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ADMIN CONTROL");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("ADMIN CONTROL");
        titleLabel.setFont(Font.font("Elephant", 25));
        titleLabel.setTextFill(Color.BLACK);

        TableView<students> tableView = new TableView<>();
        tableView.setPrefWidth(920);

        TableColumn<students, String> titleCol = new TableColumn<>("ID STUDENTS");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        titleCol.setPrefWidth(200);

        TableColumn<students, String> authorCol = new TableColumn<>("STUDENTS NAME");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        authorCol.setPrefWidth(250);

        TableColumn<students, String> stokCol = new TableColumn<>("EMAIL");
        stokCol.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        stokCol.setPrefWidth(250);

        TableColumn<students, String> idbookCol = new TableColumn<>("DEPARTMENT");
        idbookCol.setCellValueFactory(new PropertyValueFactory<>("userDepart"));
        idbookCol.setPrefWidth(150);

        TableColumn<students, Void> deleteCol = new TableColumn<>("Hapus");
        deleteCol.setPrefWidth(70);
        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Hapus");

            {
                btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;"));

                btn.setOnAction(e -> {
                    students std = getTableView().getItems().get(getIndex());
                    deleteBook(std);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                }else{
                    setGraphic(btn);
                }
            }
        });
        tableView.getColumns().clear();

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setTableMenuButtonVisible(false);
        tableView.getColumns().addAll(titleCol, authorCol, stokCol, idbookCol, deleteCol);
        loadData();
        tableView.setItems(masterData);

        HBox tombol = new HBox(10);
        tombol.setPadding(new Insets(0, 10, 0, 10));
        tombol.setAlignment(Pos.CENTER);

        Button kembali = new Button("BACK");
        kembali.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        kembali.setOnMouseExited(e -> kembali.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        kembali.setOnMouseEntered(e -> kembali.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        Button listBook = new Button("Daftar Buku");
        listBook.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        listBook.setOnMouseExited(e -> listBook.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        listBook.setOnMouseEntered(e -> listBook.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        tombol.getChildren().addAll(kembali, listBook);

        kembali.setOnAction(e ->{
            LoginGUI login = new LoginGUI();
            login.start(new Stage());
            primaryStage.close();
        });

        listBook.setOnAction(e->{
            adminBookManage bookManage = new adminBookManage();
            try {
                bookManage.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            primaryStage.close();
        });

        Image image1 = new Image("admin1.png");
        ImageView bgIV = new ImageView(image1);
        bgIV.setFitWidth(1200);
        bgIV.setFitHeight(600);
        bgIV.setPreserveRatio(false);
        bgIV.setOpacity(0.7);

        StackPane mainroot = new StackPane();

        root.getChildren().addAll(titleLabel, tableView, tombol);
        mainroot.getChildren().addAll(bgIV, root);
        Scene scene = new Scene(mainroot, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void loadData(){
        masterData.clear();
        String queryInformatika = "SELECT * FROM studentsdata";

        try(Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            try(PreparedStatement ps = conn.prepareStatement(queryInformatika);
                ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    String userid = rs.getString("idUser");
                    String username = rs.getString("nameUser");
                    String useremail = rs.getString("email");
                    String userdepart = rs.getString("jurusan");
                    masterData.add(new students(userid, username, useremail, userdepart));
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void deleteBook(students std){
        String getQuery = "DELETE FROM peminjaman WHERE idUser = ?";
        String getDelete = "DELETE FROM studentsdata WHERE idUser = ?";
        try(Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
            PreparedStatement stmt = conn.prepareStatement(getQuery)){
            stmt.setString(1, std.getUserId());
            stmt.executeUpdate();
            try (PreparedStatement stmt1 = conn.prepareStatement(getDelete)){
                stmt1.setString(1, std.getUserId());
                stmt1.executeUpdate();
            }catch (SQLException ex){
                loadData();
            }
            loadData();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
