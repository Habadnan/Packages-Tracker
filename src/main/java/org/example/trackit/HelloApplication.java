package org.example.trackit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.*;
import com.google.cloud.firestore.*;

public class HelloApplication extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    private final FirestoreContext contextFirebase = new FirestoreContext();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader masterLoader = new FXMLLoader(getClass().getResource("master-page.fxml"));
        Parent masterRoot = masterLoader.load();
        MasterPageController masterController = masterLoader.getController();
        masterRoot.setUserData(masterController);

        FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("main-page.fxml"));
        Parent content = contentLoader.load();
        masterController.setContent(content);

        Scene scene = new Scene(masterRoot, 1200, 800);

        // Attach the CSS stylesheet here
        scene.getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
