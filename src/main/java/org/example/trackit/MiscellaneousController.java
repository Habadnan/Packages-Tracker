package org.example.trackit;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MiscellaneousController implements Initializable {

    @FXML
    private Label termsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTermsFromFile();
    }

    private void loadTermsFromFile() {
        try {
            // Look for /terms-of-service.txt on the classpath (target/classes)
            URL url = getClass().getResource("/termsofservice.txt");
            if (url == null) {
                termsLabel.setText("Terms of service file not found on classpath.");
                return;
            }

            String content = Files.readString(Path.of(url.toURI()), StandardCharsets.UTF_8);
            termsLabel.setText(content);
        } catch (Exception e) {
            termsLabel.setText("Terms of service could not be loaded.");
        }
    }
}
