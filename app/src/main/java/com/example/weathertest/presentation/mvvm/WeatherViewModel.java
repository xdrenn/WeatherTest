package com.example.weathertest.presentation.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.data.repository.ApiRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class WeatherViewModel extends ViewModel {

    public final ApiRepository repository;

    @Inject
    WeatherViewModel(ApiRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<ApiResponse> weatherByCity = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> weatherByCoordinates = new MutableLiveData<>();

    public LiveData<ApiResponse> getWeatherByCity() {
        return weatherByCity;
    }

    public LiveData<ApiResponse> getWeatherByCoordinates() {
        return weatherByCoordinates;
    }

    public void initWeatherRequestCity(String city) {
        repository.loadCurrentWeatherByCity(city).subscribe(new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ApiResponse response) {
                weatherByCity.postValue(response);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void initWeatherRequestCoordinates(double lat, double lon) {
        repository.loadCurrentWeatherByCoordinates(lat, lon).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull ApiResponse apiResponse) {
                weatherByCoordinates.postValue(apiResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }
}
