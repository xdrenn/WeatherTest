package com.example.weathertest.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {

    @SerializedName("weather")
    private List<Weather> weather = null;

    @SerializedName("main")
    private Main main;

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("wind")
    private Wind wind;

    public ApiResponse(String cityName,
                       Double temp,
                       String weatherData) {
        Main main = new Main();
        main.setTemp(temp);
        Weather weather = new Weather();
        weather.setMain(weatherData);
        ArrayList<Weather> weathersList = new ArrayList<>();
        weathersList.add(weather);
        setName(cityName);
        setMain(main);
        setWeather(weathersList);
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
