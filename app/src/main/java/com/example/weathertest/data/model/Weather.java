package com.example.weathertest.data.model;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    private Integer id;

    @SerializedName("main")
    private String main;

    @SerializedName("icon")
    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}