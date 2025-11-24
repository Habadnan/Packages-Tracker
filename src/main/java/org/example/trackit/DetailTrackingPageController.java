package org.example.trackit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DetailTrackingPageController {


    private TrackingPageController.TrackingInfo trackingInfo;

    // Call this from the loader!
    public void setTrackingInfo(TrackingPageController.TrackingInfo info) {
        this.trackingInfo = info;
        populateDetails();
    }

    // Add some FXML targets for demo (e.g. add <Label fx:id="trackingNumLabel"/> in your FXML)
    @FXML
    private Label trackingNumLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private VBox itemsBox;
    @FXML
    private Label orderDateLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label addressLabel;
    // ...add more as needed

    private void populateDetails() {
        if (trackingInfo == null) return;

        // Example: set your FXML labels to show info
        if (trackingNumLabel != null)
            trackingNumLabel.setText("Tracking ID: " + trackingInfo.trackingNumber);
        if (statusLabel != null)
            statusLabel.setText(trackingInfo.status);
        if (locationLabel != null)
            locationLabel.setText(trackingInfo.location);
        if (addressLabel != null)
            addressLabel.setText(trackingInfo.location);


        itemsBox.getChildren().clear(); // Clear previous labels if any
        for (String item : trackingInfo.items.split(",")) {
            Label itemLabel = new Label("• " + item.trim());
            itemLabel.getStyleClass().add("detail-value-label");
            itemsBox.getChildren().add(itemLabel);
        }
    }

}
