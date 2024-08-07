package com.weatherapp.application.util;

import com.weatherapp.application.exception.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.weatherapp.application.Constants.ExceptionMessages;

public class ValidationUtil {

    public static void validateCity(String city) {
        if (StringUtils.isEmpty(city)) {
            throw new InvalidParameterException(ExceptionMessages.INVALID_CITY);
        }
    }

    public static void validateCountryCode(String countryCode) {
        if (StringUtils.isEmpty(countryCode) || countryCode.length() != 2) {
            throw new InvalidParameterException(
                    ExceptionMessages.INVALID_COUNTRY_CODE,
                    Map.of("countryCode", countryCode)
            );
        }
    }

}
