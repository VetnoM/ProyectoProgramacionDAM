module com.mycompany.practicabasededatos {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql; 

    opens com.mycompany.practicabasededatos to javafx.fxml;
    exports com.mycompany.practicabasededatos;
}
