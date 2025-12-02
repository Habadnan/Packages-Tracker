package org.example.trackit;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShippingPageController {

    @FXML private TextField lengthField;
    @FXML private TextField widthField;
    @FXML private TextField heightField;
    @FXML private TextField productsField;
    @FXML private TextField weightField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea userAddressArea;
    @FXML private TextField shippingLocationField;
    @FXML private Label resultLabel;

    // Simple in‑memory store of created shipments
    private static final List<Shipment> SHIPMENTS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    @FXML
    private void handleCreateShipment() {
        String length = lengthField.getText();
        String width  = widthField.getText();
        String height = heightField.getText();
        String products = productsField.getText();
        String weight = weightField.getText();
        String status = statusCombo.getValue();
        String userAddress = userAddressArea.getText();
        String shippingLocation = shippingLocationField.getText();

        // Basic validation (you can expand this)
        if (products == null || products.isBlank()
                || weight == null || weight.isBlank()
                || userAddress == null || userAddress.isBlank()
                || shippingLocation == null || shippingLocation.isBlank()
                || status == null) {
            resultLabel.setText("Please fill in all required fields.");
            return;
        }

        String trackingNumber = generateTrackingNumber();
        LocalDate createdDate = LocalDate.now();

        Shipment shipment = new Shipment(
                trackingNumber,
                createdDate.toString(),
                length, width, height,
                products,
                weight,
                status,
                userAddress,
                shippingLocation
        );
        SHIPMENTS.add(shipment);

        resultLabel.setText("Shipment created. Tracking #: " + trackingNumber);

        // Optional: clear fields after save
        // clearForm();
    }

    private String generateTrackingNumber() {
        // 9‑digit random number, padded with leading zeros if needed
        int num = 100_000_000 + RANDOM.nextInt(900_000_000);
        return String.valueOf(num);
    }

    private void clearForm() {
        lengthField.clear();
        widthField.clear();
        heightField.clear();
        productsField.clear();
        weightField.clear();
        statusCombo.getSelectionModel().clearSelection();
        userAddressArea.clear();
        shippingLocationField.clear();
    }

    // Simple data class
    public static class Shipment {
        public final String trackingNumber;
        public final String createdDate;
        public final String length;
        public final String width;
        public final String height;
        public final String products;
        public final String weight;
        public final String status;
        public final String userAddress;
        public final String shippingLocation;

        public Shipment(String trackingNumber, String createdDate,
                        String length, String width, String height,
                        String products, String weight, String status,
                        String userAddress, String shippingLocation) {
            this.trackingNumber = trackingNumber;
            this.createdDate = createdDate;
            this.length = length;
            this.width = width;
            this.height = height;
            this.products = products;
            this.weight = weight;
            this.status = status;
            this.userAddress = userAddress;
            this.shippingLocation = shippingLocation;
        }
    }
}
