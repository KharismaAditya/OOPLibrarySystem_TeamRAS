module OOPLibrarySystem.TeamRAS {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.controls;


    exports ui.adminSection;
    exports ui.loginSection;
    exports ui.profileSection;
    exports ui.programSection.dataBook;
    exports model;
}