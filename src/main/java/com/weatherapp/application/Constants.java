package com.weatherapp.application;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static class Headers {
        public static final String API_KEY = "X-API-KEY";
    }

    public static class OpenWeatherMap {
        public static final String FETCH_GEO_CODE_ENDPOINT = "/geo/1.0/direct?q=%s,%s&limit=1&appid=%s";
        public static final String FETCH_WEATHER_DATA_ENDPOINT = "/data/2.5/weather?lat=%s&lon=%s&appid=%s";
    }

    public final static DateTimeFormatter PERIOD_CODE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHH");

    public static class HttpStatusCode {
        public static final int NOT_FOUND = 404;
    }

}
