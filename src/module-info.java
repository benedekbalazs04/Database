module DatabaseExample {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens sample.database;
    opens sample.editGUI;
    opens sample;
}