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

    @FXML private TextField senderFirstNameField;
    @FXML private TextField senderLastNameField;
    @FXML private TextArea senderAddressField;
    @FXML private TextField senderZipField;

    @FXML private TextField receiverFirstNameField;
    @FXML private TextField receiverLastNameField;
    @FXML private TextArea shippingLocationField;
    @FXML private TextField receiverZipField;


    @FXML private Label resultLabel;

    // Simple in‑memory store of created shipments
    private static final List<Shipment> SHIPMENTS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    @FXML
    private void handleCreateShipment() {
        String length  = lengthField.getText();
        String width   = widthField.getText();
        String height  = heightField.getText();
        String products = productsField.getText();
        String weight   = weightField.getText();

        String senderFirst  = senderFirstNameField.getText();
        String senderLast   = senderLastNameField.getText();
        String senderAddress = senderAddressField.getText();
        String senderZip = senderZipField.getText();

        String receiverFirst = receiverFirstNameField.getText();
        String receiverLast  = receiverLastNameField.getText();
        String shippingLocation = shippingLocationField.getText();
        String receiverZip = receiverZipField.getText();

        // Basic validation – require all the new fields as well
        if (products == null || products.isBlank()
                || weight == null || weight.isBlank()
                || senderFirst == null || senderFirst.isBlank()
                || senderLast == null || senderLast.isBlank()
                || senderAddress == null || senderAddress.isBlank()
                || senderZip == null || senderZip.isBlank()
                || receiverFirst == null || receiverFirst.isBlank()
                || receiverLast == null || receiverLast.isBlank()
                || shippingLocation == null || shippingLocation.isBlank()
                || receiverZip == null || receiverZip.isBlank()
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
                senderFirst, senderLast, senderAddress, senderZip,
                receiverFirst, receiverLast, receiverZip,
                shippingLocation
        );
        SHIPMENTS.add(shipment);

        resultLabel.setText("Shipment created. Tracking #: " + trackingNumber);

        // Optional: clear fields after save
        // clearForm();
    }
    //FAKE RIGHT NOW
    private String generateTrackingNumber() {
        int num = 100_000_000 + RANDOM.nextInt(900_000_000);
        return String.valueOf(num);
    }


    private void clearForm() {
        lengthField.clear();
        widthField.clear();
        heightField.clear();
        productsField.clear();
        weightField.clear();
        senderFirstNameField.clear();
        senderLastNameField.clear();
        receiverFirstNameField.clear();
        receiverLastNameField.clear();
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
        protected final String senderFirstName;
        protected final String senderLastName;
        protected final String senderAddress;
        protected final String senderZip;
        protected final String receiverFirstName;
        protected final String receiverLastName;
        protected final String receiverZip;
        protected final String shippingLocation;

        protected Shipment(String trackingNumber, String createdDate,
                           String length, String width, String height,
                           String products, String weight,
                           String senderFirstName, String senderLastName,
                           String senderAddress, String senderZip,
                           String receiverFirstName, String receiverLastName,
                           String receiverZip,
                           String shippingLocation) {
            this.trackingNumber = trackingNumber;
            this.createdDate = createdDate;
            this.length = length;
            this.width = width;
            this.height = height;
            this.products = products;
            this.weight = weight;
            this.senderFirstName = senderFirstName;
            this.senderLastName = senderLastName;
            this.senderAddress = senderAddress;
            this.senderZip = senderZip;
            this.receiverFirstName = receiverFirstName;
            this.receiverLastName = receiverLastName;
            this.receiverZip = receiverZip;
            this.shippingLocation = shippingLocation;
        }
    }
}
