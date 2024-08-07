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
    public static class ExceptionMessages {
        public static final String INVALID_API_KEY = "Invalid API Key Provided.";
        public static final String RATE_EXCEEDED_API_KEY = "API Key usage limit reached for the period.";
        public static final String EXPIRED_API_KEY = "Provided API Key Expired.";
        public static final String INVALID_COUNTRY_CODE = "Invalid country code provided. ISO_3166-2 country code expected";
        public static final String INVALID_CITY = "City can't empty.";
        public static final String LOCATION_NOT_AVAILABLE = "Provided location not available.";
    }

}
