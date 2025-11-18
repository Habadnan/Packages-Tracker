package org.example.trackit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MasterPageController {


    @FXML
    public Button trackingButtonHeader;
    public MenuButton trackingMenuButton;
    public MenuItem ongoingTrackingMenuItem;
    public MenuItem pastTrackingMenuItem;

    @FXML private StackPane contentPane;
    public void setContent(Node content) {
        contentPane.getChildren().setAll(content);
    }

    @FXML
    private void handleLoginSignup(ActionEvent event) {
        loadAndSetContent("login-page.fxml");
    }

    @FXML
    private void handleOngoingTracking(ActionEvent event) {
        loadAndSetContent("ongoing-tracking-page.fxml");
    }

    @FXML
    private void handlePastTracking(ActionEvent event) {
        loadAndSetContent("past-tracking-page.fxml");
    }








    //Public method to load and set content from the master page
    public void loadAndSetContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//Create display name for signed-in user
    @FXML private Button loginSignupButton;

    private boolean loggedIn = false;
    private String userDisplayName = "Login / Signup";

    // Call this after successful login/signup
    public void setUserLoggedIn(String displayName) {
        loggedIn = true;
        userDisplayName = displayName;
        updateLoginSignupButton();
    }

    private void updateLoginSignupButton() {
        if (loggedIn) {
            loginSignupButton.setText(userDisplayName);
        } else {
            loginSignupButton.setText("Login / Signup");
        }
    }
}
