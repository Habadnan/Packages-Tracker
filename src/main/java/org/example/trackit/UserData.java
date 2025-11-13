package org.example.trackit;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserData {
    private static final String FILE_PATH = "users.txt";

    // Save user credentials (username, password)
    public static void saveUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load users from file into a map
    public static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            // Ignore, means no users yet
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Check credentials
    public static boolean checkCredentials(String username, String password) {
        Map<String, String> users = loadUsers();
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
