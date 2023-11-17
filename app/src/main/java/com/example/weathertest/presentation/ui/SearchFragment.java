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
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathertest.R;
import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.databinding.FragmentSearchBinding;
import com.example.weathertest.local.DatabaseManager;
import com.example.weathertest.presentation.adapters.CitiesAdapter;
import com.example.weathertest.presentation.mvvm.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment implements CitiesAdapter.OnClickListener {

    private FragmentSearchBinding binding;
    private WeatherViewModel weatherViewModel;
    private ActivityResultLauncher<String> pLauncher;
    private ForecastFragment fragment;
    private DatabaseManager databaseManager;
    private Bundle bundle;
    private CitiesAdapter adapter;
    private List<ApiResponse> list;

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
        binding.buttonLocation.setOnClickListener(v -> getLocation());
    }

    @Override
    public void onResume() {
        super.onResume();
        databaseManager.openDb();
        getFromDb();
        initSearchView();
    }

    @SuppressLint("SetTextI18n")
    public void requestWeatherByCity(String city) {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        try {
            weatherViewModel.initWeatherRequestCity(city);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void getFromDb() {
        list = new ArrayList<>();
        adapter = new CitiesAdapter(getContext());
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        for (String cities : databaseManager.getFromDb()) {
            requestWeatherByCity(cities);
        }

            weatherViewModel.getWeatherByCity().observe(getViewLifecycleOwner(), response -> {
                list.add(response);

                if (list.size() == databaseManager.getFromDb().size()) {
                    adapter.updateData(list);
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            binding.recyclerView.setLayoutManager(linearLayoutManager);
            adapter.setClickListener(this);
            binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position, ApiResponse model) {
        bundle.putString("cityFromDb", model.getName());
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }

    private void initSearchView() {
        binding.sv.clearFocus();
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

    private void getLocation() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new WeatherLocationListener();
        final boolean fineLocationNotAllowed = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        final boolean coarseLocationNotAllowed = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
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
    }

    private void permissionListener() {
        pLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        });
    }

    Boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermission() {
        if (!isPermissionGranted()) {
            permissionListener();
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

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
            case BUTTON_POSITIVE ->
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseManager.closeDb();
    }
}