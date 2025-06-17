package ui.adminSection;
import javafx.scene.image.ImageView;
import method.TableLoad;
import model.*;
import Properties.databaseConnect;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Date;


public class adminLogPeminjaman extends Application implements TableLoad {
    private ObservableList<log> masterData = FXCollections.observableArrayList();
    databaseConnect db = new databaseConnect();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("LOG BUKU");

        VBox root = new VBox(40);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));

        Label logTitle = new Label("RIWAYAT PEMINJAMAN");
        logTitle.setFont(Font.font("Tahoma", 24));
        logTitle.setTextFill(Color.BLACK);

        TableView<log> tableView = new TableView<>();
        tableView.setPrefWidth(800);

        TableColumn<log, Integer> idPemCol = new TableColumn<>("ID PINJAM");
        idPemCol.setCellValueFactory(new PropertyValueFactory<>("idpeminjaman"));
        idPemCol.setPrefWidth(100);

        TableColumn<log, String> idBukuCol = new TableColumn<>("ID BUKU");
        idBukuCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        idBukuCol.setPrefWidth(100);

        TableColumn<log, String> judulCol = new TableColumn<>("JUDUL");
        judulCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        judulCol.setPrefWidth(200);

        TableColumn<log, Date> tglPinjamCol = new TableColumn<>("TGL PINJAM");
        tglPinjamCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
        tglPinjamCol.setPrefWidth(150);

        TableColumn<log, Date> tglReturnCol = new TableColumn<>("TGL KEMBALI");
        tglReturnCol.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
        tglReturnCol.setPrefWidth(150);

        TableColumn<log, String> idUserCol = new TableColumn<>("ID USER");
        idUserCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        idUserCol.setPrefWidth(100);


        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setTableMenuButtonVisible(false);
        tableView.getColumns().addAll(idPemCol, idBukuCol, judulCol, tglPinjamCol, tglReturnCol, idUserCol);
        loadBookDB();
        tableView.setItems(masterData);

        HBox buttonSection = new HBox(10);
        buttonSection.setPadding(new Insets(0,10,0,10));

        Button back = new Button("BACK");
        back.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        back.setOnMouseExited(e -> back.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        back.setOnMouseEntered(e -> back.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        Button delete = new Button("BERSIHKAN LOG");
        delete.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        delete.setOnMouseExited(e -> delete.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        delete.setOnMouseEntered(e -> delete.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        buttonSection.getChildren().addAll(back, delete);

        delete.setOnAction(e->{
            deleteLog();
        });

        back.setOnAction(e -> {
            adminClassManage admin = new adminClassManage();
            try {
                admin.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        root.getChildren().addAll(tableView, buttonSection);
        StackPane mainroot = new StackPane();

        ImageView bgIV = new ImageView("admin1.png");
        bgIV.setFitWidth(1200);
        bgIV.setFitHeight(600);
        bgIV.setPreserveRatio(true);
        bgIV.setOpacity(0.9);

        mainroot.getChildren().addAll(bgIV, root);
        Scene scene = new Scene(mainroot, 1200, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void loadBookDB(){
        masterData.clear();
        String queryPeminjaman = "SELECT * FROM peminjaman";
        String queryBook1 = "SELECT * FROM informaticbook WHERE idBuku = ?";
        String queryBook2 = "SELECT * FROM machinebook WHERE idBuku = ?";

        try (Connection conn = db.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(queryPeminjaman)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int idPinjam = rs.getInt("idpeminjaman");
                    String idBuku = rs.getString("idBuku");
                    Date tglPinjam = rs.getDate("tanggalPinjam");
                    Date tglKembali = rs.getDate("tanggalKembali");
                    String idUser = rs.getString("idUser");

                    String judul = "", penulis = "";

                    // Coba cari di informaticbook
                    try (PreparedStatement psBook = conn.prepareStatement(queryBook1)) {
                        psBook.setString(1, idBuku);
                        ResultSet rsBook = psBook.executeQuery();
                        if (rsBook.next()) {
                            judul = rsBook.getString("judul");
                        }
                    }

                    // Kalau tidak ditemukan, coba di machinebook
                    if (judul.isEmpty()) {
                        try (PreparedStatement psBook = conn.prepareStatement(queryBook2)) {
                            psBook.setString(1, idBuku);
                            ResultSet rsBook = psBook.executeQuery();
                            if (rsBook.next()) {
                                judul = rsBook.getString("judul");
                            }
                        }
                    }
                    masterData.add(new log(idPinjam, idBuku, judul, tglPinjam,tglKembali,idUser));
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

    public void deleteLog(){
        String query = "DELETE FROM peminjaman WHERE tanggalKembali IS NOT NULL";
        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ps.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Penghapusan Log");
            alert.setHeaderText(null);
            alert.setContentText("LOG BERHASIL DIHAPUS");
            alert.showAndWait();

            loadBookDB();
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }
    }

}
