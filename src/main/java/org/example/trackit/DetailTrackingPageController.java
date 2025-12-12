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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class DetailTrackingPageController {

    @FXML
    public Button backButton;
    private TrackingPageController.TrackingInfo trackingInfo;
    private String trackingID;
    private String packageType;

    // Call this from the loader!
    public void setTrackingInfo(TrackingPageController.TrackingInfo info) {
        this.trackingInfo = info;
        DocumentReference docRef = HelloApplication.fstore.collection("shipments").document(info.trackingNumber);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                if(document.getData().get("SenderID").equals(MasterPageController.userLoggedIn.getUid())){
                    packageType = "Shipping";
                }
                else{
                    packageType = "Receiving";
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        populateDetails();
    }
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

        // Example: set your FXML labels to show info
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