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

![Alt text](https://github.com/kokilaw/weather-query-service/blob/task/db-setup-and-init-scheme/misc/db-diagram.png)

 - `api_key` table will be maintaining the current API keys and `status` column with track whether they are active.
 - `access_log` table will track the usage of API keys.
 - `weather_update` table will store the weather updates received from OpenWeatherMap API. A unique key combining the columns `city, country_code and period_code` is used to determine whether and weather update is available locally for a given hour a certain date. If available, update will be sourced from DB otherwise OpenWeatherMap API will be used.
 - Spring AOP has been used to validate and track the API Key usage. API Key protected endpoints will be annotated with `@RateControlled`

## Future Enhancements

- Move the database credentials and external API keys from `application.yaml` to AWS SecretManager and load the from the environment variables.
- Implement caching to reduce the database reads.



