package org.buzz.swervesim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwerveSimApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SwerveSimApplication.class.getResource("swerve-sim-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 600);
        SwerveSimViewController viewController = fxmlLoader.getController();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        SwerveSimController appController = new SwerveSimController(viewController, new SwerveCommand());
    }

    public static void main(String[] args) {
        launch();
    }
}