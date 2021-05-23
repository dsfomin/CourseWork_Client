package com.gpsplus.georef;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyLocationListener implements LocationListener {
    private final ClientActivity.ClientThread clientThread;
    private final Context context;

    public MyLocationListener(ClientActivity.ClientThread clientThread, Context context) {
        this.clientThread = clientThread;
        this.context = context;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LocationDTO myLocation = new LocationDTO(location.getLatitude(), location.getLongitude(), location.getAltitude());
        System.out.println(myLocation);
        Toast.makeText(context, "" + myLocation, Toast.LENGTH_SHORT).show();
        clientThread.sendMessage(myLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
