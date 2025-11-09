package org.example.trackit;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {
    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private void handleLogin() {
        System.out.println("Logging in: " + loginUsernameField.getText());
    }
}
