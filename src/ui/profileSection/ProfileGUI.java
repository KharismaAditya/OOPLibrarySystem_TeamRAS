package ui.profileSection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public ObservableList<Return> masterData = FXCollections.observableArrayList();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Diri");

        HBox mainroot = new HBox(20);
        mainroot.setPadding(new Insets(40));
        mainroot.setAlignment(Pos.TOP_CENTER);
        mainroot.setStyle("-fx-background-color : #60B5FF");

        TableView<Return> tableView = new TableView<>();
        tableView.setPrefWidth(750);

        //columns
        TableColumn<Return, Integer> idBorCol = new TableColumn<>("ID PINJAM");
        idBorCol.setCellValueFactory(new PropertyValueFactory<>("idPeminjaman"));
        idBorCol.setPrefWidth(100);

        TableColumn<Return, String> idBukuCol = new TableColumn<>("ID BUKU");
        idBukuCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        idBukuCol.setPrefWidth(100);

        TableColumn<Return, Date> tglPinjamCol = new TableColumn<>("Tanggal Pinjam");
        tglPinjamCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));
        tglPinjamCol.setPrefWidth(150);

        TableColumn<Return, String> judulCol = new TableColumn<>("Judul");
        judulCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        judulCol.setPrefWidth(200);

        TableColumn<Return, String> penulisCol = new TableColumn<>("Penulis");
        penulisCol.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        penulisCol.setPrefWidth(100);

        TableColumn<Return, Void> returnCol = new TableColumn<>("Kembalikan");
        returnCol.setPrefWidth(80);
        returnCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Kembalikan");

            {
                btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;"));

                btn.setOnAction(e -> {

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



        tableView.getColumns().addAll(idBorCol, idBukuCol, tglPinjamCol, judulCol, penulisCol, returnCol);
        loadBookFromDB();
        FilteredList<Return> filteredData = new FilteredList<>(masterData, b -> true);
        tableView.setItems(filteredData);

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

        Button kembaliButton = new Button("Keluar");
        kembaliButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        kembaliButton.setOnMouseEntered(e -> kembaliButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        kembaliButton.setOnMouseExited(e -> kembaliButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        pinjamBukuButton.setOnAction(e -> {
            primaryStage.close();
        });

        root.getChildren().addAll(titleLabel, grid, pinjamBukuButton, kembaliButton);
        mainroot.getChildren().addAll(root, tableView);
        Scene scene = new Scene(mainroot, 1200, 800);
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

    private void loadBookFromDB() {
        masterData.clear();
        String queryPeminjaman = "SELECT * FROM peminjaman WHERE idUser = ?";
        String queryBook1 = "SELECT * FROM informaticbook WHERE idBuku = ?";
        String queryBook2 = "SELECT * FROM machinebook WHERE idBuku = ?";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            try (PreparedStatement ps = conn.prepareStatement(queryPeminjaman)) {
                ps.setString(1, idUser);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int idPinjam = rs.getInt("idpeminjaman");
                    String idBuku = rs.getString("idBuku");
                    Date tglPinjam = rs.getDate("tanggalpinjam");

                    String judul = "", penulis = "";

                    // Coba cari di informaticbook
                    try (PreparedStatement psBook = conn.prepareStatement(queryBook1)) {
                        psBook.setString(1, idBuku);
                        ResultSet rsBook = psBook.executeQuery();
                        if (rsBook.next()) {
                            judul = rsBook.getString("judul");
                            penulis = rsBook.getString("penulis");
                        }
                    }

                    // Kalau tidak ditemukan, coba di machinebook
                    if (judul.isEmpty()) {
                        try (PreparedStatement psBook = conn.prepareStatement(queryBook2)) {
                            psBook.setString(1, idBuku);
                            ResultSet rsBook = psBook.executeQuery();
                            if (rsBook.next()) {
                                judul = rsBook.getString("judul");
                                penulis = rsBook.getString("penulis");
                            }
                        }
                    }
                    masterData.add(new Return(idPinjam,idBuku,tglPinjam,judul,penulis));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}