package org.example.trackit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader masterLoader = new FXMLLoader(getClass().getResource("master-page.fxml"));
        Parent masterRoot = masterLoader.load();
        MasterPageController masterController = masterLoader.getController();
        masterRoot.setUserData(masterController); // Ensure accessibility

        FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("main-page.fxml"));
        Parent content = contentLoader.load();
        masterController.setContent(content);

        stage.setScene(new Scene(masterRoot, 1200, 800));
        stage.setMaximized(true);
        stage.show();
    }
}
