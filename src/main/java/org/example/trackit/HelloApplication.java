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
        // start firebase
        fstore = contextFirebase.firebase();
        fauth = FirebaseAuth.getInstance();

        FXMLLoader masterLoader = new FXMLLoader(getClass().getResource("master-page.fxml"));
        Parent masterRoot = masterLoader.load();
        MasterPageController masterController = masterLoader.getController();
        masterRoot.setUserData(masterController);

        // INITIAL CONTENT *** use loadAndSetContent instead of setContent ***
        masterController.loadAndSetContent("guest-main-page.fxml");

        Scene scene = new Scene(masterRoot, 1200, 800);
        scene.getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
