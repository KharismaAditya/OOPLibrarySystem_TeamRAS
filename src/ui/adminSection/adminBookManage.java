package ui.adminSection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import method.TableLoad;
import model.Book;
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


public class adminBookManage extends Application implements TableLoad {
    databaseConnect db = new databaseConnect();

    private ObservableList<Book> masterData = FXCollections.observableArrayList();


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("ADMIN CONTROL");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("ADMIN CONTROL");
        titleLabel.setFont(Font.font("Elephant", 25));
        titleLabel.setTextFill(Color.BLACK);

        ComboBox<String> bookType = new ComboBox<>();
        bookType.getItems().addAll("Informatika", "Teknik Mesin");
        bookType.setValue("Informatika");
        bookType.valueProperty().addListener((obs, oldVal, newVal) -> {
            String tableName = newVal.equals("Informatika") ? "informaticbook" : "machinebook";
            loadBookDB(tableName);
        });

        TableView<Book> tableView = new TableView<>();
        tableView.setPrefWidth(720);

        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        titleCol.setPrefWidth(250);

        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        authorCol.setPrefWidth(200);

        TableColumn<Book, Integer> stokCol = new TableColumn<>("Stok");
        stokCol.setCellValueFactory(new PropertyValueFactory<>("stok"));
        stokCol.setPrefWidth(100);

        TableColumn<Book, String> idbookCol = new TableColumn<>("ID BOOK");
        idbookCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
        idbookCol.setPrefWidth(100);
        TableColumn<Book, Void> deleteCol = new TableColumn<>("Hapus");
        deleteCol.setPrefWidth(70);
        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Hapus");

            {
                btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;"));

                btn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    String query = "";
                    if(bookType.getValue().equals("Informatika")){
                        query = "informaticbook";
                    }else if(bookType.getValue().equals("Teknik Mesin")){
                        query = "machinebook";
                    }

                    deleteBook(book, query);
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

        tableView.getColumns().addAll(titleCol, authorCol, stokCol, idbookCol, deleteCol);
        String queryBook = "";
        if(bookType.getValue().equals("Informatika")){
            queryBook = "informaticbook";
            loadBookDB(queryBook);
        }else if(bookType.getValue().equals("Teknik Mesin")){
            queryBook = "machinebook";
            loadBookDB(queryBook);
        }
        tableView.setItems(masterData);

        Label tambahBukuLabel = new Label("TAMBAH BUKU");
        tambahBukuLabel.setTextFill(Color.BLACK);
        tambahBukuLabel.setFont(Font.font("Elephant", 18));

        //tambahBuku
        Label judulLabel = new Label("Judul Buku: ");
        judulLabel.setTextFill(Color.BLACK);
        TextField judulField = new TextField();
        judulField.setPromptText("ENTER BOOK TITLE");judulField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8;");

        Label penulisLabel = new Label("Penulis Buku: ");
        penulisLabel.setTextFill(Color.BLACK);
        TextField penulisField = new TextField();
        penulisField.setPromptText("ENTER AUTHOR NAME");penulisField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8;");

        Label stokLabel = new Label("Masukkan Stok buku: ");
        stokLabel.setTextFill(Color.BLACK);
        TextField stokField = new TextField();
        stokField.setPromptText("ENTER STOCK");stokField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8;");

        Label idBukuLabel = new Label("Masukkan id Buku: ");
        idBukuLabel.setTextFill(Color.BLACK);
        TextField idBukuField = new TextField();
        idBukuField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5; -fx-padding: 8;");idBukuField.setPromptText("ENTER BOOK ID");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setMaxWidth(350);

        grid.add(judulLabel, 0, 0);
        grid.add(judulField, 1, 0);
        grid.add(penulisLabel, 0, 1);
        grid.add(penulisField, 1, 1);
        grid.add(stokLabel, 0, 2);
        grid.add(stokField, 1, 2);
        grid.add(idBukuLabel, 0, 3);
        grid.add(idBukuField, 1, 3);

        Button tambah = new Button("ADD");
        tambah.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        tambah.setOnMouseExited(e -> tambah.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        tambah.setOnMouseEntered(e -> tambah.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        Button kembali = new Button("BACK");
        kembali.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        kembali.setOnMouseExited(e -> kembali.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        kembali.setOnMouseEntered(e -> kembali.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        GridPane gridButton = new GridPane();
        gridButton.setAlignment(Pos.CENTER);
        gridButton.setVgap(15);
        gridButton.setHgap(10);
        gridButton.setMaxWidth(350);

        gridButton.add(tambah, 0, 0);
        gridButton.add(kembali, 0, 1);

        HBox sectionBelow =new HBox(10);
        sectionBelow.getChildren().addAll(grid, gridButton);
        sectionBelow.setAlignment(Pos.CENTER);

        VBox addSection = new VBox(10);
        addSection.setPadding(new Insets(0, 0, 0, 20));
        addSection.setAlignment(Pos.CENTER_LEFT);
        addSection.getChildren().addAll(tambahBukuLabel, sectionBelow);


        tambah.setOnAction(e ->{
            String query = "";
            if(bookType.getValue().equals("Informatika")){
                query = "informaticbook";
            }else if(bookType.getValue().equals("Teknik Mesin")){
                query = "machinebook";
            }
            String Judul = judulField.getText();
            String idBuku = idBukuField.getText();
            if (Judul.isEmpty() || penulisField.getText().isEmpty() || stokField.getText().isEmpty() || idBuku.isEmpty()) {
                errorAnnounce("Data harus diisi lengkap");
                return;
            }

            int stokBuku;
            try {
                stokBuku = Integer.parseInt(stokField.getText());
                if (stokBuku < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                errorAnnounce("Stok harus berupa angka positif");
                judulField.clear();
                penulisField.clear();
                stokField.clear();
                idBukuField.clear();
                return;
            }

            if (checkJudul(query, Judul)) {
                errorAnnounce("Judul Buku sudah tersedia");
            } else if (checkbookId(query, idBuku)) {
                errorAnnounce("Id buku sudah terpakai");
            } else {
                addBook(query, Judul, penulisField.getText(), stokBuku, idBuku);
            }

            judulField.clear();
            penulisField.clear();
            stokField.clear();
            idBukuField.clear();
        });

        kembali.setOnAction(e ->{
            adminClassManage CM = new adminClassManage();
            try {
                CM.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            primaryStage.close();
        });

        Image image1 = new Image("bookmanage.png");
        ImageView bgIV = new ImageView(image1);
        bgIV.setFitWidth(1200);
        bgIV.setFitHeight(600);
        bgIV.setPreserveRatio(true);
        bgIV.setOpacity(0.9);

        StackPane mainroot = new StackPane();

        HBox pageUnder = new HBox(10);
        pageUnder.setPadding(new Insets(10));
        pageUnder.setAlignment(Pos.CENTER);
        pageUnder.getChildren().addAll(tableView, addSection);

        root.getChildren().addAll(titleLabel,bookType, pageUnder);
        mainroot.getChildren().addAll(bgIV, root);


        Scene scene = new Scene(mainroot, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void loadBookDB() {

    }

    @Override
    public void loadBookDB(String query){
        masterData.clear();
        String queryInformatika = "SELECT * FROM " + query;

        try(Connection conn = db.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(queryInformatika);
                ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    String judul = rs.getString("judul");
                    String penulis = rs.getString("penulis");
                    int stok = rs.getInt("stok");
                    String idBuku = rs.getString("idBuku");
                    masterData.add(new Book(judul, penulis, stok, idBuku));
                }
            }
        } catch(IOException | SQLException e){
            e.printStackTrace();
        }
    }

    private void addBook(String query, String judul, String penulis, int stok, String idBuku){
        String getQuery = "INSERT INTO " + query + " (judul, penulis, stok, idBuku) VALUES (?, ?, ?, ?)";
        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(getQuery)){
            stmt.setString(1, judul);
            stmt.setString(2, penulis);
            stmt.setInt(3, stok);
            stmt.setString(4, idBuku);
            stmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Penambahan buku");
            alert.setHeaderText(null);
            alert.setContentText("Buku berhasil ditambahkan");
            alert.showAndWait();
            loadBookDB(query);
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }

    private void deleteBook(Book book, String query){
        String getQuery = "DELETE FROM " + query + " WHERE idBuku = ?";
        String getDelete = "DELETE FROM peminjaman WHERE idBuku = ?" ;
        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(getQuery)){
            stmt.setString(1, book.getIdBuku());
            stmt.executeUpdate();
            try (PreparedStatement stmt1 = conn.prepareStatement(getDelete)){
                stmt1.setString(1, book.getIdBuku());
                stmt1.executeUpdate();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Penghapusan Buku");
            alert.setHeaderText(null);
            alert.setContentText("Buku berhasil dihapus");
            alert.showAndWait();
            loadBookDB(query);
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }

    private boolean checkJudul(String query, String judul){
        String query1 = "SELECT * FROM " + query + " WHERE judul = ?";
        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query1)){

            stmt.setString(1, judul);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkbookId(String query, String bookId){
        String query1 = "SELECT * FROM " + query + " WHERE idBuku = ?";
        try(Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query1)){

            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void errorAnnounce(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
//note