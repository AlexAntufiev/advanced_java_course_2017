package edu.technopolis.advanced.weather.dao;

import edu.technopolis.advanced.weather.domain.entity.Weather;
import edu.technopolis.advanced.weather.domain.entity.WeatherFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WeatherDao {

    private static final Logger log = LoggerFactory.getLogger(WeatherFactory.class);

    private static final int KELVIN = 273;

    private static final String BASE_URL = "api.openweathermap.org/data/2.5/weather?q=";

    public Weather getWeather(String city) throws IOException, JSONException {
        Weather weather = new Weather();
        JSONObject response = getResponseObject(city);
        weather.setCity(city);
        weather.setTemperature(response.getJSONObject("main").getDouble("temp") - KELVIN);
        weather.setDescription(response.getJSONArray("weather").getString(2));
        return weather;
    }

    private JSONObject getResponseObject(String city) throws IOException, JSONException {
        final String url = BASE_URL + city;
        log.info("url = " + url);
        return WeatherService.read(url);
    }
}