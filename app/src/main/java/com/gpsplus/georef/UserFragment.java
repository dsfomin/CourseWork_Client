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

import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;

public class UserFragment extends Fragment {

    public static final int SERVER_PORT = 9093;
    public static final String SERVER_IP = "192.168.1.127";

    private LinearLayout locationsList;
    private TextView longitude;
    private TextView latitude;
    private TextView altitude;
    private Button connectToServer;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        locationsList = view.findViewById(R.id.user_connection_info_list);
        locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
        longitude = view.findViewById(R.id.user_edit_text_longitude);
        latitude = view.findViewById(R.id.user_edit_text_latitude);
        altitude = view.findViewById(R.id.user_edit_text_altitude);
        connectToServer = view.findViewById(R.id.user_connect_server_button);

        connectToServer.setOnClickListener(v -> {
            locationsList.removeAllViews();

            longitude.setText("0");
            latitude.setText("0");
            altitude.setText("0");

            UserThread thread = new UserThread(getContext(), locationsList, getActivity());
            new Thread(thread).start();

            locationListener = new MyLocationUserListener(longitude, latitude, altitude);

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

        view.findViewById(R.id.user_to_main_button).setOnClickListener(
                v -> NavHostFragment.findNavController(UserFragment.this)
                        .navigate(R.id.action_UserFragment_to_MainFragment));
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

