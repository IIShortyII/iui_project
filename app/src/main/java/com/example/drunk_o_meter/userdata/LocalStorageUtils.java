package com.example.drunk_o_meter.userdata;


import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LocalStorageUtils {

    /**
     * Check if a file exists
     * @param filename The name of the file to be checked
     * @return The flag if the file exists
     */
    public static boolean hasFile(String filename) {
        File file = new File(filename);
        return file.isFile();
    }

    /**
     * Helper function to store a file to the private local storage
     * @param context Context like the main activity
     * @param filename Name of the file to be created
     * @param content Content as string to be written in the file
     * @throws IOException
     */
    public static void saveFile(Context context, String filename, String content) throws IOException {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Helper function to load a file from the private local storage
     * @param context Context like the main activity
     * @param filename Name of the file to be loaded
     * @return The content of the file as string
     * @throws FileNotFoundException
     */
    public static String loadFile(Context context, String filename) throws FileNotFoundException {
        FileInputStream fis = context.openFileInput(filename);
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        String contents;
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            contents = stringBuilder.toString();
        }
        return contents;
    }

}

