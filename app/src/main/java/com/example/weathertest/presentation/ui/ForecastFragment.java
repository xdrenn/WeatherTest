package com.example.weathertest.presentation.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weathertest.databinding.FragmentForecastBinding;
import com.example.weathertest.presentation.mvvm.WeatherViewModel;
import com.example.weathertest.utils.Constants;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;

    private WeatherViewModel weatherViewModel;

    private Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        assert getArguments() != null;
        double lat = getArguments().getDouble("lat");
        double lon = getArguments().getDouble("lon");
        String cityFromDb = getArguments().getString("cityFromDb");
        String cityFromSearch = getArguments().getString("cityFromSearch");


        if(cityFromDb != null) {
            requestWeatherByCity(cityFromDb);
        }
        if(cityFromSearch != null){
            requestWeatherByCity(cityFromSearch);
        }
        requestWeatherByCoord(lat, lon);
    }

    @SuppressLint("SetTextI18n")
    public void requestWeatherByCity(String city) {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherViewModel.initWeatherRequestCity(city, Constants.APIKEY);
        weatherViewModel.getWeatherByCity().observe(getViewLifecycleOwner(), string -> {
                binding.temperature.setText(string.getMain().getTemp().toString());
                binding.city.setText(string.getName());
        });
    }

    @SuppressLint("SetTextI18n")
    public void requestWeatherByCoord(double lat, double lon) {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherViewModel.initWeatherRequestCoord(lat, lon, Constants.APIKEY);
        weatherViewModel.getWeatherByCoord().observe(getViewLifecycleOwner(), string ->
                binding.condition.setText(string.getMain().getTemp().toString()));

    }
}