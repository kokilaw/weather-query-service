# weather-query-service

## Requirements

For building and running the application you need:
- Java 21
- Spring Boot 3.3.2

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.weatherapp.application.WeatherQueryServiceApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
./mvnw spring-boot:run
```
## Testing
 - Load your OpenWeatherMap API key as an environment variable into `OPEN_WEATHER_API_KEY` variable.
 - You can use following curl to fetch the weather update replacing `{REPLACE_WITH_API_KEY}` placeholder with the API Key. You can use one the following API keys which are loaded during the startup.

```shell
curl --location 'localhost:8080/v1/weather?city=melbourne&countryCode=au' \
--header 'X-API-KEY: {REPLACE_WITH_API_KEY}'
```
```
5cf9dd49-c5f2-4634-893a-23a31abf2bdc
76002345-0196-47d1-8ee7-743b4c7ce86f
7e967fbb-4fb9-4ed5-847b-de0c032bb23f
570ace6f-b516-459c-9a24-25b8b2f8e576
744443ad-9fcd-40b8-ae49-01c6838d5041
```


## Design Decisions

![Alt text](https://github.com/kokilaw/weather-query-service/blob/task/db-setup-and-init-scheme/misc/db-diagram.png)

 - `api_key` table will be maintaining the current API keys and `status` column with track whether they are active.
 - `access_log` table will track the usage of API keys.
 - `weather_update` table will store the weather updates received from OpenWeatherMap API. A unique key combining the columns `city, country_code and period_code` is used to determine whether and weather update is available locally for a given hour a certain date. If available, update will be sourced from DB otherwise OpenWeatherMap API will be used. 
 - `period_code` value will be saved in the format – `yyyyMMddHH`. (E.g., 2024080613)
 - Spring AOP has been used to validate and track the API Key usage. API Key protected endpoints will be annotated with `@RateControlled`

## Future Enhancements

- Move the database credentials and external API keys from `application.yaml` to AWS SecretManager and load them from the environment variables. 
- Incorporate caching to reduce database reads on frequently fetched locations.



