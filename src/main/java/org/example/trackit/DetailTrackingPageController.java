package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class DetailTrackingPageController {

    @FXML
    private Button backButton;
    private TrackingPageController.TrackingInfo trackingInfo;
    private String trackingID;
    private String packageType;

    @FXML
    private Label daysRemainingLabel;
    @FXML
    private ProgressBar deliveryProgressBar;
    @FXML
    private Label progressLabel;


    // Call this from the loader!
    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
        DocumentReference docRef = HelloApplication.fstore
                .collection("shipments")
                .document(trackingID);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                LocalDate estimated = LocalDate.parse(document.get("CreatedDate").toString()).plusDays(5);
                trackingInfo = new TrackingPageController.TrackingInfo(
                        document.get("TrackingNumber").toString(),
                        document.get("CreatedDate").toString(),
                        document.get("Products").toString(),
                        document.get("ShippingLocation").toString(),
                        estimated.toString(),
                        document.get("Status").toString()
                );
                if(document.getData().get("SenderID").equals(MasterPageController.userLoggedIn.getUid())){
                    packageType = "Shipped by You";
                }
                else{
                    packageType = "Received by You";
                }
                populateDetails(); // <-- add this line
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Add some FXML targets for demo (e.g. add <Label fx:id="trackingNumLabel"/> in your FXML)
    @FXML
    private Label trackingNumLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label packageTypeLabel;
    @FXML
    private VBox itemsBox;
    @FXML
    private Label orderDateLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label estDateLabel;
    // ...add more as needed

    private void populateDetails() {
        if (trackingInfo == null) return;

        //set FXML labels to show info
        if (trackingNumLabel != null)
            trackingNumLabel.setText("Tracking ID: " + trackingInfo.trackingNumber);
        if (statusLabel != null)
            statusLabel.setText(trackingInfo.status);
        if(packageTypeLabel != null)
            packageTypeLabel.setText(packageType);
        if (locationLabel != null)
            locationLabel.setText(trackingInfo.location);
        if (addressLabel != null)
            addressLabel.setText(trackingInfo.location);
        if(orderDateLabel != null)
            orderDateLabel.setText(trackingInfo.orderDate);
        if(estDateLabel != null)
            estDateLabel.setText(trackingInfo.estimatedDate);

        itemsBox.getChildren().clear(); // Clear previous labels if any
        for (String item : trackingInfo.items.split(",")) {
            Label itemLabel = new Label("• " + item.trim());
            itemLabel.getStyleClass().add("detail-value-label");
            itemsBox.getChildren().add(itemLabel);
        }

        // ----- Progress calculation -----
        try {
            LocalDate created = LocalDate.parse(trackingInfo.orderDate);
            LocalDate estimated = LocalDate.parse(trackingInfo.estimatedDate);
            LocalDate today = LocalDate.now();

            long totalDays = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(created, estimated));
            long daysPassed = java.time.temporal.ChronoUnit.DAYS.between(created, today);
            long daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(today, estimated);

            // Clamp values
            if (daysPassed < 0) daysPassed = 0;
            if (daysPassed > totalDays) daysPassed = totalDays;

            double progress = (double) daysPassed / (double) totalDays;

            if (deliveryProgressBar != null)
                deliveryProgressBar.setProgress(progress);   // value 0..1[web:53]
            if (daysRemainingLabel != null)
                daysRemainingLabel.setText(Long.toString(Math.max(0, daysRemaining)));
            if (progressLabel != null)
                progressLabel.setText(String.format("Progress: %d%%", Math.round(progress * 100)));

        } catch (Exception e) {
            // If parsing fails, show indeterminate bar
            if (deliveryProgressBar != null)
                deliveryProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            if (progressLabel != null)
                progressLabel.setText("Progress: N/A");
        }

    }
    public void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
            masterController.goBackOnePage();
        }
    }
}