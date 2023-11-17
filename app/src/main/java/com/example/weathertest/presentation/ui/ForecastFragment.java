package com.example.weathertest.presentation.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weathertest.R;
import com.example.weathertest.databinding.FragmentForecastBinding;
import com.example.weathertest.presentation.mvvm.WeatherViewModel;
import com.example.weathertest.utils.Constants;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;
    private WeatherViewModel weatherViewModel;
    private SearchFragment fragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new SearchFragment();
    }

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
        binding.backButton.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit());

        if (cityFromDb != null) {
            requestWeatherByCity(cityFromDb);
        }
        if (cityFromSearch != null) {
            requestWeatherByCity(cityFromSearch);
        }
        if (cityFromSearch == null && cityFromDb == null) {
            requestWeatherByCoordinates(lat, lon);
        }
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

        weatherViewModel.getWeatherByCity().observe(getViewLifecycleOwner(), weather -> {
            binding.temperature.setText(Math.round(weather.getMain().getTemp()) + "°");
            binding.city.setText(weather.getName());
            weather.getWeather().forEach(condition -> binding.condition.setText(condition.getMain()));
            weather.getWeather().forEach(icon -> Picasso.get().load(Constants.ICON_URL + icon.getIcon() + ".png").into(binding.weatherIcon));
            binding.minTemp.setText("L:" + Math.round(weather.getMain().getTempMin()) + "°");
            binding.maxTemp.setText("H:" + Math.round(weather.getMain().getTempMax()) + "°");
            binding.tempFeelsLike.setText(Math.round(weather.getMain().getFeelsLike()) + "°");
            binding.humidity.setText(weather.getMain().getHumidity() + "%");
            binding.windSpeed.setText(weather.getWind().getSpeed().toString() + "m/s");
        });
    }

    @SuppressLint("SetTextI18n")
    public void requestWeatherByCoordinates(double lat, double lon) {
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        try {
            weatherViewModel.initWeatherRequestCoordinates(lat, lon);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

        weatherViewModel.getWeatherByCoordinates().observe(getViewLifecycleOwner(), weather -> {
            binding.temperature.setText(Math.round(weather.getMain().getTemp()) + "°");
            binding.city.setText(weather.getName());
            weather.getWeather().forEach(condition -> binding.condition.setText(condition.getMain()));
            weather.getWeather().forEach(icon -> Picasso.get().load(Constants.ICON_URL + icon.getIcon() + ".png").into(binding.weatherIcon));
            binding.minTemp.setText("L:" + Math.round(weather.getMain().getTempMin()) + "°");
            binding.maxTemp.setText("H:" + Math.round(weather.getMain().getTempMax()) + "°");
            binding.tempFeelsLike.setText(Math.round(weather.getMain().getFeelsLike()) + "°");
            binding.humidity.setText(weather.getMain().getHumidity() + "%");
            binding.windSpeed.setText(weather.getWind().getSpeed().toString() + "m/s");
        });
    }
}