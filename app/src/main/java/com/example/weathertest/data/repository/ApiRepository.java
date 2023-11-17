package com.example.weathertest.data.repository;

import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.data.remote.ApiService;
import com.example.weathertest.utils.Constants;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class ApiRepository {

    private final ApiService apiService;

    @Inject
    public ApiRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Observable<ApiResponse> loadCurrentWeatherByCity(String city) {
        return apiService.getWeatherByCity(city, Constants.APIKEY, Constants.UNITS);
    }

    public Single<ApiResponse> loadCurrentWeatherByCoordinates(double lat, double lon) {
        return apiService.getWeatherByCoordinates(lat, lon, Constants.APIKEY, Constants.UNITS);
    }
}
