package com.example.weathertest.presentation.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.data.remote.ApiService;
import com.example.weathertest.data.repository.WeatherRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class WeatherViewModel extends ViewModel {

    public final WeatherRepository weatherRepository;

    public final ApiService apiService;

    @Inject
    WeatherViewModel(WeatherRepository weatherRepository, ApiService apiService){
        this.weatherRepository = weatherRepository;
        this.apiService = apiService;
    }

    private final MutableLiveData<ApiResponse> weatherByCity = new MutableLiveData<>();

    public LiveData<ApiResponse> getWeatherByCity() {
        return weatherByCity;
    }

    private final MutableLiveData<ApiResponse> weatherByCoord = new MutableLiveData<>();

    public LiveData<ApiResponse> getWeatherByCoord(){
        return weatherByCoord;
    }

    public void initWeatherRequestCity(String city, String apikey) {
        weatherRepository.loadCurrentWeatherByCity(city, apikey).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ApiResponse apiResponse) {
                weatherByCity.postValue(apiResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    public void initWeatherRequestCoord(double lat, double lon, String apikey){
        weatherRepository.loadCurrentWeatherByCoord(lat, lon, apikey).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ApiResponse apiResponse) {
                weatherByCoord.postValue(apiResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
}
