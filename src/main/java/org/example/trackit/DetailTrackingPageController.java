package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class DetailTrackingPageController {

    @FXML private ImageView mapImageView;

    @FXML private Label trackingNumLabel;
    @FXML private Label statusLabel;
    @FXML private Label packageTypeLabel;
    @FXML private VBox itemsBox;
    @FXML private Label orderDateLabel;
    @FXML private Label locationLabel;
    @FXML private Label addressLabel;
    @FXML private Label estDateLabel;
    @FXML private Label daysRemainingLabel;
    @FXML private ProgressBar deliveryProgressBar;
    @FXML private Label progressLabel;

    private String trackingID;
    private TrackingPageController.TrackingInfo trackingInfo;
    private String packageType;

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;

        DocumentReference docRef =
                HelloApplication.fstore.collection("shipments").document(trackingID);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (!document.exists()) return;

            LocalDate estimated =
                    LocalDate.parse(document.get("CreatedDate").toString()).plusDays(5);

            trackingInfo = new TrackingPageController.TrackingInfo(
                    document.get("TrackingNumber").toString(),
                    document.get("CreatedDate").toString(),
                    document.get("Products").toString(),
                    document.get("ShippingLocation").toString(),
                    estimated.toString(),
                    document.get("Status").toString()
            );

            packageType =
                    document.get("SenderID")
                            .equals(MasterPageController.userLoggedIn.getUid())
                            ? "Shipped by You"
                            : "Received by You";

            populateDetails();

            String origin =
                    document.get("SenderAddress") + " " + document.get("SenderZip");
            String destination =
                    document.get("ShippingLocation") + " " + document.get("ReceiverZip");

            loadMap(origin, destination);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void populateDetails() {
        trackingNumLabel.setText("Tracking ID: " + trackingInfo.trackingNumber);
        statusLabel.setText(trackingInfo.status);
        packageTypeLabel.setText(packageType);
        locationLabel.setText(trackingInfo.location);
        addressLabel.setText(trackingInfo.location);
        orderDateLabel.setText(trackingInfo.orderDate);
        estDateLabel.setText(trackingInfo.estimatedDate);

        itemsBox.getChildren().clear();
        for (String item : trackingInfo.items.split(",")) {
            Label l = new Label("• " + item.trim());
            l.getStyleClass().add("detail-value-label");
            itemsBox.getChildren().add(l);
        }

        LocalDate created = LocalDate.parse(trackingInfo.orderDate);
        LocalDate estimated = LocalDate.parse(trackingInfo.estimatedDate);
        LocalDate today = LocalDate.now();

        long totalDays = Math.max(1,
                java.time.temporal.ChronoUnit.DAYS.between(created, estimated));
        long passed =
                java.time.temporal.ChronoUnit.DAYS.between(created, today);

        passed = Math.min(Math.max(passed, 0), totalDays);

        double progress = (double) passed / totalDays;

        deliveryProgressBar.setProgress(progress);
        daysRemainingLabel.setText(
                String.valueOf(Math.max(0,
                        java.time.temporal.ChronoUnit.DAYS.between(today, estimated)))
        );
        progressLabel.setText("Progress: " + Math.round(progress * 100) + "%");
    }

    private void loadMap(String origin, String destination) {
        new Thread(() -> {
            try {
                double[] from = GeocodingUtil.geocode(origin);
                double[] to = GeocodingUtil.geocode(destination);

                String mapUrl =
                        StaticMapUtil.buildMap(
                                from[0], from[1],
                                to[0], to[1]
                        );

                Image mapImage = new Image(mapUrl, true);

                Platform.runLater(() ->
                        mapImageView.setImage(mapImage)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController controller =
                    (MasterPageController) masterRoot.getUserData();
            controller.goBackOnePage();
        }
    }
}
