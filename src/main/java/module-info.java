module org.example.cab302_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens org.example.cab302_project to javafx.fxml;
    exports org.example.cab302_project;
}