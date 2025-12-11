package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TrackingPageController {

    @FXML
    private GridPane trackingListGrid;

    @FXML
    public void initialize() {
        //        List<TrackingInfo> trackingData = List.of(
//                new TrackingInfo("123456789", "11/14/2025", "Wireless headphone, phone case", "Warehouse", "11/17/2025", "On the way"),
//                new TrackingInfo("987654321", "11/15/2025", "Shoes", "Sorting Facility", "11/18/2025", "Delayed"),
//                new TrackingInfo("555111222", "11/16/2025", "Book, Charger, Cable, Adapter, Power Bank, Mouse, Keyboard", "Out for delivery", "11/20/2025", "Out for delivery"),
//                new TrackingInfo("444333222", "11/16/2025", "Tablet, Pen", "Local Office", "11/20/2025", "In transit"),
//                new TrackingInfo("111000999", "11/17/2025", "Monitor, Stand", "Delivery Truck", "11/22/2025", "Delivered"),
//                new TrackingInfo("111000999", "11/17/2025", "Monitor, Stand", "Delivery Truck", "11/22/2025", "Delivered"),
//                new TrackingInfo("111000999", "11/17/2025", "Monitor, Stand", "Delivery Truck", "11/22/2025", "Delivered"),
//                new TrackingInfo("111000999", "11/17/2025", "Monitor, Stand", "Delivery Truck", "11/22/2025", "Delivered"),
//                new TrackingInfo("111000999", "11/17/2025", "Monitor, Stand", "Delivery Truck", "11/22/2025", "Delivered"),
//                new TrackingInfo("111000999", "11/17/2025", "Monitor, Stand", "Delivery Truck", "11/22/2025", "Delivered")
//
//        );

        List<TrackingInfo> trackingData = new ArrayList<>();
        if(UserMainPageController.clickedOngoing){
            //fill the trackingData list with packages that do not have the status "Delivered"
            populateWithOngoing(trackingData);
        }
        else{
            //user clicked past packages so only fill list with past packages that have the status "Delivered"
            populateWithDelivered(trackingData);
        }
        int colNum = 3;
        trackingListGrid.getChildren().clear();

        for (int i = 0; i < trackingData.size(); i++) {
            TrackingInfo info = trackingData.get(i);
            Button cardButton = createTrackingInfoButton(info);
            int col = i % colNum;
            int row = i / colNum;
            trackingListGrid.add(cardButton, col, row);
        }
    }

    private Button createTrackingInfoButton(TrackingInfo info) {
        VBox card = new VBox();
        card.getStyleClass().add("tracking-info-card");
        card.setPrefWidth(320);
        card.setPrefHeight(410);
        card.setSpacing(0);

        // Top row: Tracking number (left), Status (right)
        HBox topRow = new HBox();
        Label trackingNum = new Label(info.trackingNumber);
        trackingNum.getStyleClass().add("tracking-label-title");
        Label status = new Label(info.status);
        status.getStyleClass().add("tracking-label-title");
        topRow.getChildren().addAll(trackingNum, new Region(), status);
        HBox.setHgrow(topRow.getChildren().get(1), Priority.ALWAYS);

        // Date row
        Label orderDateTitle = new Label("Ordered on:");
        orderDateTitle.getStyleClass().add("tracking-label-item-title");

        Label orderDate = new Label(info.orderDate);
        orderDate.getStyleClass().add("tracking-label-section");

        Label itemsLabel = new Label("Items:");
        itemsLabel.getStyleClass().add("tracking-label-item-title");

        Label items = new Label(info.items);
        items.getStyleClass().add("tracking-label-section");

        Label locationLabel = new Label("Location:");
        locationLabel.getStyleClass().add("tracking-label-item-title");

        Label location = new Label(info.location);
        location.getStyleClass().add("tracking-label-section");

        Label estLabel = new Label("Est. Delivery Date:");
        estLabel.getStyleClass().add("tracking-label-item-title");

        Label estimated = new Label(info.estimatedDate);
        estimated.getStyleClass().add("tracking-label-section");

        card.getChildren().addAll(topRow, orderDateTitle, orderDate, itemsLabel, items, locationLabel, location, estLabel, estimated);

        // Turn card into a real clickable button
        Button cardButton = new Button();
        cardButton.setGraphic(card);
        cardButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        cardButton.setPrefSize(320, 410);
        cardButton.setFocusTraversable(false);

        cardButton.setOnAction(evt -> {
            // OPEN  detailed page
            showTrackingDetail(info);
        });

        return cardButton;
    }

    private void showTrackingDetail(TrackingInfo info) {
        try {
            // Load the detail page FXML and controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detail-tracking-page.fxml"));
            Parent detailRoot = loader.load();

            // Set the data for the detail page
            DetailTrackingPageController controller = loader.getController();
            controller.setTrackingInfo(info);

            // Find the current MasterPageController via UserData (like in your LoginPageController)
            Stage stage = (Stage) trackingListGrid.getScene().getWindow();
            Parent masterRoot = stage.getScene().getRoot();
            if (masterRoot != null) {
                MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
                masterController.setContent(detailRoot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void populateWithOngoing(List<TrackingInfo> list){
        ApiFuture<QuerySnapshot> future = HelloApplication.fstore.collection("shipments").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                for (QueryDocumentSnapshot document : documents) {
                    if (!(document.getData().get("Status").equals("Delivered"))
                            && document.getData().get("ReceiverID").equals(MasterPageController.userLoggedIn.getUid())
                            || document.getData().get("SenderID").equals(MasterPageController.userLoggedIn.getUid())) {
                        LocalDate estimated = LocalDate.parse(document.get("CreatedDate").toString()).plusDays(5);
                        list.add(new TrackingInfo(document.get("TrackingNumber").toString(), document.get("CreatedDate").toString(), document.get("Products").toString(),
                                document.get("ShippingLocation").toString(), estimated.toString(), document.get("Status").toString()));
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    private void populateWithDelivered(List<TrackingInfo> list) {
        ApiFuture<QuerySnapshot> future = HelloApplication.fstore.collection("shipments").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                for (QueryDocumentSnapshot document : documents) {
                    if (document.getData().get("Status").equals("Delivered")
                            && (document.getData().get("ReceiverID").equals(MasterPageController.userLoggedIn.getUid())
                            || document.getData().get("SenderID").equals(MasterPageController.userLoggedIn.getUid()))) {
                        LocalDate estimated = LocalDate.parse(document.get("CreatedDate").toString()).plusDays(5);
                        list.add(new TrackingInfo(document.get("TrackingNumber").toString(), document.get("CreatedDate").toString(), document.get("Products").toString(),
                                document.get("ShippingLocation").toString(), estimated.toString(), document.get("Status").toString()));
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }


    public static class TrackingInfo {
        public final String trackingNumber, orderDate, items, location, estimatedDate, status;
        public TrackingInfo(String t, String o, String i, String l, String e, String s) {
            trackingNumber = t; orderDate = o; items = i; location = l; estimatedDate = e; status = s;
        }
    }
}
