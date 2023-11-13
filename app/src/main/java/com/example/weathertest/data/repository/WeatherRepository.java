package com.example.weathertest.data.repository;

import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.data.remote.ApiService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;


public class WeatherRepository {

    private final ApiService apiService;


    @Inject
    public WeatherRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<ApiResponse> loadCurrentWeatherByCity(String city, String apikey){
        return apiService.getWeatherByCity(city, apikey);
    }

    public Single<ApiResponse> loadCurrentWeatherByCoord(double lat, double lon, String apikey){
        return apiService.getWeatherByCoordinates(lat, lon, apikey);
    }
}
