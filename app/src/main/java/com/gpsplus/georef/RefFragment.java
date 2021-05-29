package com.gpsplus.georef;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class RefFragment extends Fragment {

    public static final int SERVER_PORT = 9092;
    public static final String SERVER_IP = "192.168.1.127";

    private LinearLayout locationsList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button connectToServer;
    private EditText longitude;
    private EditText latitude;
    private EditText altitude;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ref, container, false);

        locationsList = view.findViewById(R.id.ref_connection_info_list);
        locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
        longitude = view.findViewById(R.id.ref_edit_text_longitude);
        latitude = view.findViewById(R.id.ref_edit_text_latitude);
        altitude = view.findViewById(R.id.ref_edit_text_altitude);
        connectToServer = view.findViewById(R.id.ref_connect_server_button);

        connectToServer.setOnClickListener(v -> {
            // TODO: check valid longitude and latitude
            locationsList.removeAllViews();
            double DLongitude = Double.parseDouble(longitude.getText().toString());
            double DLatitude = Double.parseDouble(latitude.getText().toString());
            double DAltitude = Double.parseDouble(altitude.getText().toString());
            RefThread thread = new RefThread(DLongitude, DLatitude, DAltitude);
            new Thread(thread).start();

            locationListener = new MyLocationRefListener(thread, getContext(), locationsList);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 10);
                }
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ref_to_main_button).setOnClickListener(
                v -> NavHostFragment.findNavController(RefFragment.this)
                        .navigate(R.id.action_RefFragment_to_MainFragment));
    }

    @SuppressLint("SetTextI18n")
    public TextView textView(String message, int color) {
        if (null == message || message.trim().isEmpty()) {
            message = "<Empty Message>";
        }
        TextView tv = new TextView(getContext());
        tv.setTextColor(color);
        tv.setText(message + " [" + getTime() + "]");
        tv.setTextSize(20);
        tv.setPadding(0, 5, 0, 0);
        return tv;
    }

    String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }
    }
}

