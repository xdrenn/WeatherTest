package com.example.weathertest.data.remote;

import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.utils.Constants;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET(Constants.CURRENT_WEATHER_END_POINT)
    Observable<ApiResponse> getWeatherByCity(
            @Query("q") String city,
            @Query("APPID") String apiKey,
            @Query("units") String units
    );

    @GET(Constants.CURRENT_WEATHER_END_POINT)
    Single<ApiResponse> getWeatherByCoordinates(@Query("lat") double lat,
                                                @Query("lon") double lon,
                                                @Query("APPID") String apiKey,
                                                @Query("units") String units);
}
