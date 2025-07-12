package com.app.invoice.configs;

import java.net.HttpURLConnection;
import java.net.URL;

public class InternetConnectivityChecker {

    public static boolean isOnline(String url, String schoolID) {
        try {
            // Try to connect to the server to check if it's reachable
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);  // 10 seconds timeout
            urlConnection.setReadTimeout(10000);     // 10 seconds timeout

            urlConnection.setRequestProperty("X-Tenant", schoolID);

            urlConnection.connect();
            return urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;  // If an error occurs (no internet), return false
        }
    }
}
