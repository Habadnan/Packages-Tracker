package org.example.trackit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.List;

public class TrackingPageController {

    @FXML
    private GridPane trackingListGrid;

    @FXML
    public void initialize() {
        List<TrackingInfo> trackingData = List.of(
                new TrackingInfo("123456789", "11/14/2025", "Wireless headphone, phone case", "Warehouse", "11/17/2025", "On the way"),
                new TrackingInfo("987654321", "11/15/2025", "Shoes", "Sorting Facility", "11/18/2025", "Delayed"),
                new TrackingInfo("555111222", "11/16/2025", "Book, Charger, Cable, Adapter, Power Bank, Mouse, Keyboard", "Out for delivery", "11/20/2025", "Out for delivery"),
                new TrackingInfo("444333222", "11/16/2025", "Tablet, Pen", "Local Office", "11/20/2025", "In transit"),
                new TrackingInfo("111000999", "11/17/2025", "Monitor, Stand", "Delivery Truck", "11/22/2025", "Delivered")
        );
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

    // Replace with real navigation logic:
    private void showTrackingDetail(TrackingInfo info) {
        System.out.println("Open detail for: " + info.trackingNumber);
        // You could call your master page's content swapper here,
        // passing the TrackingInfo as needed.
    }

    public static class TrackingInfo {
        public final String trackingNumber, orderDate, items, location, estimatedDate, status;
        public TrackingInfo(String t, String o, String i, String l, String e, String s) {
            trackingNumber = t; orderDate = o; items = i; location = l; estimatedDate = e; status = s;
        }
    }
}
