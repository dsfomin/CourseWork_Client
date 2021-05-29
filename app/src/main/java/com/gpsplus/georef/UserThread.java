package com.gpsplus.georef;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpsplus.geoserver.Correction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserThread implements Runnable {

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private final Context context;
    private final LinearLayout locationsList;
    private final Activity activity;
    private static LocationDTO currentLocation;

    public UserThread(Context context, LinearLayout locationsList, Activity activity) {
        this.context = context;
        this.locationsList = locationsList;
        this.activity = activity;
    }

    @Override
    public void run() {

        try {
            InetAddress serverAddress = InetAddress.getByName(UserFragment.SERVER_IP);
            socket = new Socket(serverAddress, UserFragment.SERVER_PORT);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            while (!Thread.currentThread().isInterrupted()) {
                Correction correction = (Correction) input.readObject();

                if (null != correction) {
                    LocationDTO correctLocation = findCorrectLocation(currentLocation, correction);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(params);
                    tv.setText(String.format("%s", correctLocation));
                    activity.runOnUiThread(() -> locationsList.addView(tv));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private LocationDTO findCorrectLocation(LocationDTO currentLocation, Correction correction) {
        return new LocationDTO(currentLocation.getLatitude() + correction.getLatitude(),
                currentLocation.getLongitude() + correction.getLongitude(),
                currentLocation.getAltitude() + correction.getAltitude());
    }

    public static void setCurrentLocation(LocationDTO currentLocation) {
        UserThread.currentLocation = currentLocation;
    }
}
