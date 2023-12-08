module rusiru.project {
    requires javafx.controls;
    requires javafx.fxml;

    opens rusiru.project to javafx.fxml;
    exports rusiru.project;
}
