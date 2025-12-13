package org.example.trackit;

public class StaticMapUtil {

    // 🔑 SAME API KEY AS ABOVE
    private static final String API_KEY = "AIzaSyARoVBH5rbUFDPoLMx1BB979OSafZFEeUs";

    public static String buildMap(
            double latA, double lngA,
            double latB, double lngB
    ) {
        return "https://maps.googleapis.com/maps/api/staticmap"
                + "?size=640x360"
                + "&maptype=roadmap"
                + "&markers=color:green|label:S|" + latA + "," + lngA
                + "&markers=color:red|label:D|" + latB + "," + lngB
                + "&path=color:0x493CD6FF|weight:5|"
                + latA + "," + lngA + "|"
                + latB + "," + lngB
                + "&key=" + API_KEY;
    }
}
