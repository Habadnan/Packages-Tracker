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
    @FXML private TextArea shippingLocationField;
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
        String shippingLocation = shippingLocationField.getText();

        // Basic validation (you can expand this)
        if (products == null || products.isBlank()
                || weight == null || weight.isBlank()
                || shippingLocation == null || shippingLocation.isBlank()
        ) {
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
        shippingLocationField.clear();
    }

    // Simple data class
    protected static class Shipment {
        protected final String trackingNumber;
        protected final String createdDate;
        protected final String length;
        protected final String width;
        protected final String height;
        protected final String products;
        protected final String weight;
        protected final String shippingLocation;

        protected Shipment(String trackingNumber, String createdDate,
                        String length, String width, String height,
                        String products, String weight, String shippingLocation) {
            this.trackingNumber = trackingNumber;
            this.createdDate = createdDate;
            this.length = length;
            this.width = width;
            this.height = height;
            this.products = products;
            this.weight = weight;
            this.shippingLocation = shippingLocation;
        }
    }
}
