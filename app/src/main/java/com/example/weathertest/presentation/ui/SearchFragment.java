package com.example.weathertest.presentation.ui;


import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.weathertest.R;
import com.example.weathertest.databinding.FragmentSearchBinding;
import com.example.weathertest.local.DatabaseManager;
import com.example.weathertest.presentation.adapters.CitiesAdapter;
import com.example.weathertest.presentation.mvvm.WeatherViewModel;
import com.example.weathertest.utils.Constants;

import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private WeatherViewModel weatherViewModel;


    private ActivityResultLauncher<String> pLauncher;
    private ForecastFragment fragment;

    private DatabaseManager databaseManager;

    private CitiesAdapter adapter;
    private Bundle bundle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();

        databaseManager = new DatabaseManager(getContext());
        fragment = new ForecastFragment();
        bundle = new Bundle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseManager.openDb();
        getFromDb();
        initSearchView();
        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    //remote and local requests
    @SuppressLint("SetTextI18n")
    public void requestWeatherByCity(String city) {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherViewModel.initWeatherRequestCity(city, Constants.APIKEY);
    }

    private void getFromDb() {
        for (String cities : databaseManager.getFromDb()) {
            requestWeatherByCity(cities);
            weatherViewModel.getWeatherByCity().observe(getViewLifecycleOwner(), response -> {
                adapter = new CitiesAdapter(List.of(response));
            });

            binding.rv.setAdapter(adapter);
            adapter.setOnClickListener((position, model) -> {
                bundle.putString("cityFromDb", model.getName());
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            });
        }
    }


    //search view initialization
    private void initSearchView() {
        binding.sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                databaseManager.insertIntoDb(query);
                bundle.putString("cityFromSearch", query);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    //check if location enabled
    private void getLocation() {

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new WeatherLocationListener();

        final boolean fineLocationNotAllowed = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        final boolean coarseLocationNotAllowed = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        final boolean networkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        final boolean gpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (fineLocationNotAllowed && coarseLocationNotAllowed) {
            openLocationDialog();
        } else {
            if (networkProviderEnabled) {
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, Looper.getMainLooper());
            } else if (gpsProviderEnabled) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, Looper.getMainLooper());
            }
        }
    }

    private class WeatherLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            bundle.putDouble("lat", lat);
            bundle.putDouble("lon", lon);
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }

    //check permissions
    private void permissionListener() {
        pLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        });
    }

    Boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermission() {
        if (!isPermissionGranted()) {
            permissionListener();
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    //ask user to enable location
    private void openLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enable location");
        builder.setMessage("Do you want enable location?");
        builder.setPositiveButton("yes", this::onClick);
        builder.setNegativeButton("no", this::onClick);
        builder.create().show();
    }

    private void onClick(DialogInterface dialog, int i) {
        switch (i) {
            case BUTTON_NEGATIVE -> dialog.dismiss();
            case BUTTON_POSITIVE -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseManager.closeDb();
    }
}