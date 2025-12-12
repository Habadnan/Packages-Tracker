package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

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
    private String trackingNumber = generateNonRepeatedTrackingNumber();
    // Simple in‑memory store of created shipments
    private static final List<Shipment> SHIPMENTS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    @FXML
    private void handleCreateShipment() {
        String length = lengthField.getText();
        String width  = widthField.getText();
        String height = heightField.getText();
        String weight = weightField.getText();
        String products = productsField.getText();
        String senderFirst  = senderFirstNameField.getText();
        String senderLast   = senderLastNameField.getText();
        String senderAddress = senderAddressField.getText();
        String senderZip = senderZipField.getText();
        String receiverFirst = receiverFirstNameField.getText();
        String receiverLast  = receiverLastNameField.getText();
        String shippingLocation = shippingLocationField.getText();
        String receiverZip = receiverZipField.getText();

        if (length == null || length.isBlank() || width == null || width.isBlank()
                || height == null || height.isBlank()
                || products == null || products.isBlank()
                || weight == null || weight.isBlank()
                || senderFirst == null || senderFirst.isBlank()
                || senderLast == null || senderLast.isBlank()
                || senderAddress == null || senderAddress.isBlank()
                || senderZip == null || senderZip.isBlank()
                || receiverFirst == null || receiverFirst.isBlank()
                || receiverLast == null || receiverLast.isBlank()
                || shippingLocation == null || shippingLocation.isBlank()
                || receiverZip == null || receiverZip.isBlank()) {
            resultLabel.setText("Please fill in all required fields.");
            return;
        }
        createPackage();
//        String trackingNumber = generateTrackingNumber();
//        LocalDate createdDate = LocalDate.now();
//
//        Shipment shipment = new Shipment(
//                trackingNumber,
//                createdDate.toString(),
//                length, width, height,
//                products,
//                weight,
//                status,
//                userAddress,
//                shippingLocation
//        );
//        SHIPMENTS.add(shipment);
        resultLabel.setText("Shipment created. Tracking #: " + trackingNumber);
        // Optional: clear fields after save
        // clearForm();
    }

    private String generateTrackingNumber() {
        // 9‑digit random number, padded with leading zeros if needed
        int num = 100_000_000 + RANDOM.nextInt(900_000_000);
        return String.valueOf(num);
    }

    //If we want to clear the form
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

    //firebase changes start here
    private void createPackage(){
        DocumentReference docRef = HelloApplication.fstore.collection("shipments").document(trackingNumber);
        Map<String, Object> data = addShipmentData();
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    private Map<String, Object> addShipmentData() {
        Map<String, Object> data = new HashMap<>();
        data.put("Length", lengthField.getText());
        data.put("Width", widthField.getText());
        data.put("Height", heightField.getText());
        data.put("Products", productsField.getText());
        data.put("Weight", weightField.getText());
        data.put("Status", "Preparing Shipment");
        data.put("ShippingLocation", shippingLocationField.getText());
        data.put("TrackingNumber", trackingNumber);
        data.put("CreatedDate", LocalDate.now().toString());
        data.put("SenderFirstName", senderFirstNameField.getText());
        data.put("SenderLastName", senderLastNameField.getText());
        data.put("SenderAddress", senderAddressField.getText());
        data.put("SenderZip", senderZipField.getText());
        data.put("SenderID", MasterPageController.userLoggedIn.getUid());
        data.put("ReceiverFirstName", receiverFirstNameField.getText());
        data.put("ReceiverLastName", receiverLastNameField.getText());
        data.put("ReceiverZip", receiverZipField.getText());
        data.put("ReceiverID", getReceiverID());
        return data;
    }

    private String generateNonRepeatedTrackingNumber() {
        String trackNum = generateTrackingNumber();
        ApiFuture<QuerySnapshot> future = HelloApplication.fstore.collection("shipments").get();
        List<QueryDocumentSnapshot> documents;
        boolean isRepeat = true;
        try {
            documents = future.get().getDocuments();
            while(isRepeat){
                if (!documents.isEmpty()) {
                    for (QueryDocumentSnapshot document : documents) {
                        if (document.getData().get("TrackingNumber").equals(trackNum)) {
                            System.out.println("Tracking number already exists in the database");
                            trackNum = generateTrackingNumber();
                        }
                        else{
                            isRepeat = false;
                        }
                    }
                }
                else{
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        trackingNumber = trackNum;
        return trackNum;
    }

    private String getReceiverID(){
        ApiFuture<QuerySnapshot> future = HelloApplication.fstore.collection("users").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                for (QueryDocumentSnapshot document : documents) {
                    if (document.getData().get("FirstName").equals(receiverFirstNameField.getText())
                    && document.getData().get("LastName").equals(receiverLastNameField.getText())
                    && document.getData().get("Address").equals(shippingLocationField.getText())
                    && document.getData().get("ZipCode").equals(receiverZipField.getText())) {
                        return document.getData().get("ID").toString();
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return "0";
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
