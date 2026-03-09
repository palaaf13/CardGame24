module com.axelpalacios.cardgame24 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.axelpalacios.cardgame24 to javafx.fxml;
    exports com.axelpalacios.cardgame24;
}