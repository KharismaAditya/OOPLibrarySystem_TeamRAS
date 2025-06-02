
package ui.programSection.dataBook;

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
import ui.profileSection.ProfileGUI;


import java.sql.*;

public class displaybook extends Application {

    private static final String dbURL = "jdbc:mysql://127.0.0.1:3306/databook";
    private static final String dbUser = "root";
    private static final String dbPass = "a2001234";
    private String mhsQuery;

    public String getMhsQuery() {
        return mhsQuery;
    }

    public void setMhsQuery(String mhsQuery) {
        this.mhsQuery = mhsQuery;
    }

    private ObservableList<Book> masterData = FXCollections.observableArrayList();


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Daftar Buku");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #60B5FF;");

        Label titleLabel = new Label("Daftar Buku");
        titleLabel.setFont(Font.font("Tahoma", 24));
        titleLabel.setTextFill(Color.BLACK);

        TextField searchField = new TextField();
        searchField.setPromptText("Cari buku berdasarkan judul atau penulis...");
        searchField.setMaxWidth(400);

        TableView<Book> tableView = new TableView<>();
        tableView.setPrefWidth(700);

        // Columns
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

        TableColumn<Book, Void> borrowCol = new TableColumn<>("Pinjam");
        borrowCol.setPrefWidth(70);
        borrowCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Pinjam");

            {
                btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold;"));

                btn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    borrowBook(book);
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


        tableView.getColumns().addAll(titleCol, authorCol, stokCol,idbookCol, borrowCol);

        loadBooksFromDB();

        FilteredList<Book> filteredData = new FilteredList<>(masterData, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lowerCaseFilter = newValue.toLowerCase();

            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                if (book.getJudul().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (book.getPenulis().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        Button back = new Button("BACK");
        back.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;");
        back.setOnMouseExited(e -> back.setStyle("-fx-background-color: #FF9149; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));
        back.setOnMouseEntered(e -> back.setStyle("-fx-background-color: #FFECDB; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 30 10 30; -fx-background-radius: 5;"));

        back.setOnAction(e -> {
            ProfileGUI profil = new ProfileGUI(getMhsQuery());
            profil.start(new Stage());
            primaryStage.close();
        });

        tableView.setItems(filteredData);

        root.getChildren().addAll(titleLabel, searchField, tableView, back);

        Scene scene = new Scene(root, 750, 650);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void borrowBook(Book book) {

    }



    private void loadBooksFromDB() {

    }
}

