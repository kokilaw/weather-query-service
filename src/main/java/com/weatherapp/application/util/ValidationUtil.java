package com.weatherapp.application.util;

import com.weatherapp.application.exception.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ValidationUtil {

    public static void validateCity(String city) {
        if (StringUtils.isEmpty(city)) {
            throw new InvalidParameterException("City can't empty.");
        }
    }

    public static void validateCountryCode(String countryCode) {
        if (StringUtils.isEmpty(countryCode) || countryCode.length() != 2) {
            throw new InvalidParameterException(
                    "Invalid country code provided. ISO_3166-2 country code expected",
                    Map.of("countryCode", countryCode)
            );
        }
    }

}
