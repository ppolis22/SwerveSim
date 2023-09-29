module org.buzz.swervesim {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.buzz.swervesim to javafx.fxml;
    exports org.buzz.swervesim;
}