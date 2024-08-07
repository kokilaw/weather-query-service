package com.weatherapp.application.util;

import com.weatherapp.application.exception.InvalidParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    public void givenEmptyCityValue_returnError() {
        InvalidParameterException invalidParameterException = assertThrows(
                InvalidParameterException.class, () -> ValidationUtil.validateCity("")
        );
        assertEquals("City can't empty.", invalidParameterException.getMessage());
    }

    @Test
    public void givenValidCityValue_doesNotReturnError() {
        assertDoesNotThrow(() -> ValidationUtil.validateCity("Perth"));
    }

    @Test
    public void givenInvalidCountryCode_returnError() {
        InvalidParameterException invalidParameterException = assertThrows(
                InvalidParameterException.class,
                () -> ValidationUtil.validateCountryCode("Australia")
        );
        assertEquals(
                "Invalid country code provided. ISO_3166-2 country code expected",
                invalidParameterException.getMessage()
        );
    }

    @Test
    public void givenValidCountryCode_doesNotReturnError() {
        assertDoesNotThrow(() -> ValidationUtil.validateCountryCode("AU"));
    }

}