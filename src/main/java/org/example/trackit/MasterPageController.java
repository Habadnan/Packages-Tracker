package org.example.trackit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MasterPageController {


//Header buttons ----------------------------------------------------------------
    @FXML
    private Button homePageButton;
    @FXML
    private Button supportButton;
    @FXML
    private Button loginSignupButton;

//Header drop down menu
    @FXML
    private MenuButton trackingMenuButton;
    @FXML
    private MenuItem ongoingTrackingMenuItem;
    @FXML
    private MenuItem pastTrackingMenuItem;
// ----------------------------------------------------------------


//Footer ----------------------------------------------------------------
    @FXML
    private Hyperlink aboutUs;
    @FXML
    private Hyperlink portfolio;
    @FXML
    private Hyperlink termsOfService;

// ----------------------------------------------------------------

    @FXML public StackPane contentPane;
    public void setContent(Node content) {
        contentPane.getChildren().setAll(content);
    }



//Header handler ----------------------------------------------------------------
    @FXML
    private void handleHeaderNav(ActionEvent event) {
        Object source = event.getSource();
        String fxmlFile = null;

        if (source == homePageButton) {
            fxmlFile = "guest-main-page.fxml";
        }
        else if (source == supportButton) {
            fxmlFile = "support-page.fxml";
        }
        else if (source == loginSignupButton) {
            fxmlFile = "login-page.fxml";
        }
        else if (source == ongoingTrackingMenuItem) {
            fxmlFile = "tracking-page.fxml";
        }
        else if (source == pastTrackingMenuItem) {
            fxmlFile = "tracking-page.fxml";
        }

        if (fxmlFile != null) {
            loadAndSetContent(fxmlFile);
        }
    }
//----------------------------------------------------------------


    @FXML
    private void handleFooter(ActionEvent event) {
        Object source = event.getSource();
        String fxmlFile = null;

        if (source == aboutUs) {
            loadAndSetContent("about-us-page.fxml");
        }
        else if (source == portfolio) {
            loadAndSetContent("portfolio-page.fxml");
        }
        else if (source == termsOfService) {
            loadAndSetContent("terms-of-service-page.fxml");
        }

        if (fxmlFile != null) {
            loadAndSetContent(fxmlFile);
        }

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
