package com.gpsplus.georef;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MyLocationRefListener implements LocationListener {
    private final RefThread refThread;
    private final Context context;
    private final LinearLayout locationsList;
    private boolean sendTrueLocation = false;

    public MyLocationRefListener(RefThread refThread, Context context, LinearLayout locationsList) {
        this.refThread = refThread;
        this.context = context;
        this.locationsList = locationsList;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LocationDTO myLocation = new LocationDTO(location.getLatitude(), location.getLongitude(), location.getAltitude());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(context);
        tv.setLayoutParams(params);
        tv.setText(String.format("%s", myLocation));
        locationsList.addView(tv);

//        if (!sendTrueLocation) {
//            refThread.sendLocation(refThread.getTrueLocationDTO());
//            sendTrueLocation = true;
//        }
        refThread.sendLocation(myLocation);
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
