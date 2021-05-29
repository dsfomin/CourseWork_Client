package com.gpsplus.georef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MyLocationUserListener implements LocationListener {
    private final TextView longitude;
    private final TextView latitude;
    private final TextView altitude;

    public MyLocationUserListener(TextView longitude, TextView latitude, TextView altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude.setText(Double.toString(location.getLongitude()));
        latitude.setText(Double.toString(location.getLatitude()));
        altitude.setText(Double.toString(location.getAltitude()));
        UserThread.setCurrentLocation(new LocationDTO(
                Double.parseDouble(latitude.getText().toString()),
                Double.parseDouble(longitude.getText().toString()),
                Double.parseDouble(altitude.getText().toString())));
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
