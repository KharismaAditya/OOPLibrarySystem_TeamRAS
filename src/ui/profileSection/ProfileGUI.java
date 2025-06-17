package ui.profileSection;
import method.TableLoad;
import model.*;
import Properties.databaseConnect;

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
import ui.programSection.dataBook.*;

import java.io.IOException;
import java.sql.*;

public class ProfileGUI extends Application implements TableLoad {
    databaseConnect db = new databaseConnect();

    private String idUser ;

    public ProfileGUI(String idUser ) {
        this.idUser  = idUser ;
    }

    public ObservableList<Return> masterData = FXCollections.observableArrayList();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Diri");

        HBox mainroot = new HBox(20);
        mainroot.setPadding(new Insets(40,10,40,10));
        mainroot.setAlignment(Pos.TOP_CENTER);

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
                    Return book = getTableView().getItems().get(getIndex());
                    kembaliBuku(book);
                    loadBookDB();
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
        loadBookDB();
        FilteredList<Return> filteredData = new FilteredList<>(masterData, b -> true);
        tableView.setItems(filteredData);

        VBox root = new VBox(40);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(0,70,0,0));

        Label titleLabel = new Label("SELAMAT DATANG");
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setFont(Font.font("Impact", 28));

        Image userImage = new Image("user.png");
        ImageView userIV = new ImageView(userImage);
        userIV.setFitHeight(80);
        userIV.setPreserveRatio(true);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setMaxWidth(350);

        //Nama component
        Label namaLabel = new Label("Nama: ");
        namaLabel.setTextFill(Color.BLACK);namaLabel.setFont(Font.font("Georgia", 12));
        Label namaValueLabel = new Label(getNama(idUser ));
        namaValueLabel.setTextFill(Color.BLACK);namaValueLabel.setFont(Font.font("Georgia", 12));

        Label nimLabel = new Label("NIM: ");
        nimLabel.setTextFill(Color.BLACK);nimLabel.setFont(Font.font("Georgia", 12));
        Label nimValueLabel = new Label(getNIM(idUser));
        nimValueLabel.setTextFill(Color.BLACK);nimValueLabel.setFont(Font.font("Georgia", 12));

        Label emailLabel = new Label("EMAIL: ");
        emailLabel.setTextFill(Color.BLACK);emailLabel.setFont(Font.font("Georgia", 12));
        Label emailValueLabel = new Label(getEmail(idUser));
        emailValueLabel.setTextFill(Color.BLACK);emailValueLabel.setFont(Font.font("Georgia", 12));

        Label departLabel = new Label("Jurusan: ");
        departLabel.setTextFill(Color.BLACK);departLabel.setFont(Font.font("Georgia", 12));
        Label departValueLabel = new Label(getDepart(idUser));
        departValueLabel.setTextFill(Color.BLACK);departValueLabel.setFont(Font.font("Georgia", 12));

        grid.add(namaLabel, 0, 0);
        grid.add(namaValueLabel,1, 0 );
        grid.add(nimLabel, 0, 1);
        grid.add(nimValueLabel, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailValueLabel, 1, 2);
        grid.add(departLabel, 0, 3);
        grid.add(departValueLabel, 1, 3);


        //Button pinjam buku
        Button pinjamBukuButton = new Button("Pinjam Buku");
        pinjamBukuButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        pinjamBukuButton.setOnMouseEntered(e -> pinjamBukuButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        pinjamBukuButton.setOnMouseExited(e -> pinjamBukuButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        Button kembaliButton = new Button("Keluar");
        kembaliButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        kembaliButton.setOnMouseEntered(e -> kembaliButton.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        kembaliButton.setOnMouseExited(e -> kembaliButton.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        kembaliButton.setOnAction(e ->{
            LoginGUI login = new LoginGUI();
            login.start(new Stage());
            primaryStage.close();
        });

        pinjamBukuButton.setOnAction(e -> {
            displaybook display = new displaybook();
            display.setMhsQuery(idUser);
            display.start(new Stage());
            primaryStage.close();
        });

        root.getChildren().addAll(titleLabel,userIV , grid, pinjamBukuButton, kembaliButton);
        Image imagePane = new Image("profile.png");
        ImageView imagePaneView = new ImageView(imagePane);
        imagePaneView.setFitWidth(1200);
        imagePaneView.setFitHeight(600);
        imagePaneView.setOpacity(0.9);

        StackPane stackRoot = new StackPane();

        mainroot.getChildren().addAll(root, tableView);
        stackRoot.getChildren().addAll(imagePaneView, mainroot);

        Scene scene = new Scene(stackRoot, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private String getNama(String idUser ) {
        String query = "SELECT nameUser FROM studentsdata WHERE idUser  = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stms = conn.prepareStatement(query)) {

            stms.setString(1, idUser);
            ResultSet rs = stms.executeQuery();
            if (rs.next()) {
                return rs.getString("nameUser");
            } else {
                return "Nama tidak ditemukan";
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String getNIM(String idUser){
        String query = "SELECT idUser FROM studentsdata WHERE idUser = ?";
        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, idUser);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getString("idUser");
            }else{
                return "NIM tidak ditemukan";
            }
        }catch (IOException | SQLException e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String getEmail(String idUser){
        String query = "SELECT email FROM studentsdata WHERE idUser  = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stms = conn.prepareStatement(query)) {

            stms.setString(1, idUser);
            ResultSet rs = stms.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            } else {
                return "Email tidak ditemukan";
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String getDepart(String idUser){
        String query = "SELECT jurusan FROM studentsdata WHERE idUser  = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stms = conn.prepareStatement(query)) {

            stms.setString(1, idUser);
            ResultSet rs = stms.executeQuery();
            if (rs.next()) {
                return rs.getString("jurusan");
            } else {
                return "Jurusan tidak ditemukan";
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    @Override
    public void loadBookDB() {
        masterData.clear();
        String queryPeminjaman = "SELECT * FROM peminjaman WHERE idUser = ? AND tanggalKembali IS NULL";
        String queryBook1 = "SELECT * FROM informaticbook WHERE idBuku = ?";
        String queryBook2 = "SELECT * FROM machinebook WHERE idBuku = ?";

        try (Connection conn = db.getConnection()) {
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
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadBookDB(String query) {

    }

    private void kembaliBuku(Return book){
        String peminjamanUpdate = "UPDATE peminjaman SET tanggalkembali = ? WHERE idBuku = ?";
        String updateStokInfor = "UPDATE informaticbook SET stok = stok +1 WHERE idBuku = ?";
        String updateStokMachine = "UPDATE machinebook SEt stok = stok + 1 WHERE idBuku = ?";

        try(Connection conn = db.getConnection()){
            conn.setAutoCommit(false);

            try(PreparedStatement pUp = conn.prepareStatement(peminjamanUpdate)){
                pUp.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                pUp.setString(2, book.getIdBuku());
                pUp.executeUpdate();
            }

            try (PreparedStatement hInfor = conn.prepareStatement(updateStokInfor)){
                hInfor.setString(1, book.getIdBuku());
                hInfor.executeUpdate();
            }

            try (PreparedStatement hMachine = conn.prepareStatement(updateStokMachine)){
                hMachine.setString(1, book.getIdBuku());
                hMachine.executeUpdate();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pengembalian Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Anda berhasil mengembalikan buku \"" + book.getJudul() + "\".");
            alert.showAndWait();

            conn.commit();

        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }
}