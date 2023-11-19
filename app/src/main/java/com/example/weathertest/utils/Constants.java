package com.example.weathertest.utils;

public class Constants {
    public static final String URL = "https://api.openweathermap.org/";
    public static final String ICON_URL = "https://openweathermap.org/img/w/";
    public static final String CURRENT_WEATHER_END_POINT = "data/2.5/weather";
    public static final String APIKEY = "9ed3426810f8174bf0b02c62d53eef00";
    public static final String UNITS = "metric";
    public static final String DEGREE = "°";
    public static final String PNG = ".png";
    public static final String PERCENT = "%";
    public static final String METERSPERSECOND = "m/s";
    public static final String LOW = "L:";
    public static final String HIGH = "H:";
    public static final String TABLE_NAME = "cities_table";
    public static final String _ID = "_id";
    public static final String CITY = "city";
    public static final String DB_NAME = "database.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + CITY + " TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
