package com.huce.hma.common.utils;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;

import okhttp3.ResponseBody;

public class Utils {
    public static boolean writeResponseBodyToDisk(ResponseBody body, String pathName) {
        try {
            File futureStudioIconFile = new File(pathName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = Files.newOutputStream(futureStudioIconFile.toPath());

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("APK UPDATE", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static <T> Object getPropertyValue(T object, String propertyName) {
        try {
            // Use reflection to get the field with the specified name
            Field field = object.getClass().getDeclaredField(propertyName);

            // Make the field accessible (in case it's private)
            field.setAccessible(true);

            // Get the value of the field for the given object
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace(); // Handle exceptions appropriately in your code
        }

        return null; // Return null if the property is not found
    }

    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) getSystemService(context, serviceClass);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    public static String fmMilisecondsToTimeString(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
