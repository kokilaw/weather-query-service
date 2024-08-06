# weather-query-service

## Requirements

For building and running the application you need:
- Java 21
- Spring Boot 3.3.2

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.weatherapp.application.WeatherQueryServiceApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
## Testing

You can use following curl to fetch the weather update replacing `{REPLACE_WITH_API_KEY}` placeholder with the API Key.

```shell
curl --location 'localhost:8080/v1/weather?city=melbourne&countryCode=au' \
--header 'X-API-KEY: {REPLACE_WITH_API_KEY}'
```

## Design Decisions




