package org.example.trackit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserMainPageController {
    @FXML
    public Button trackButtonDirect;
    @FXML
    public Button trackButtonOngoing;
    @FXML
    public Button trackButtonPast;
    @FXML
    public Button shipButton;


    @FXML
    public void handleButton(ActionEvent event) {
        Object source = event.getSource();
        String fxmlFile = null;

        if (source == trackButtonDirect) {
            fxmlFile = "detail-tracking-page.fxml";
        }
        else if (source == trackButtonOngoing) {
            fxmlFile = "tracking-page.fxml";
        }
        else if (source == trackButtonPast) {
            fxmlFile = "tracking-page.fxml";
        }
        else if (source == shipButton) {
            fxmlFile = "shipping-page.fxml";
        }
        if (fxmlFile != null) {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Parent masterRoot = stage.getScene().getRoot();
            if (masterRoot != null) {
                MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
                masterController.loadAndSetContent(fxmlFile);
            }
        }

    }
}
