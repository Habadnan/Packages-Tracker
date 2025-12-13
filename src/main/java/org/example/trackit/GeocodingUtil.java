package org.example.trackit;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GeocodingUtil {

    // 🔑 PUT YOUR GOOGLE MAPS API KEY HERE
    private static final String API_KEY = "AIzaSyARoVBH5rbUFDPoLMx1BB979OSafZFEeUs";

    public static double[] geocode(String address) throws Exception {
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        String urlStr =
                "https://maps.googleapis.com/maps/api/geocode/json?address="
                        + encodedAddress + "&key=" + API_KEY;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject json = new JSONObject(response.toString());
        JSONObject location = json
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        return new double[]{
                location.getDouble("lat"),
                location.getDouble("lng")
        };
    }
}
