package edu.technopolis.advanced.weather.domain.entity;

import edu.technopolis.advanced.weather.dao.WeatherDao;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WeatherFactory {
    private static final Logger log = LoggerFactory.getLogger(WeatherFactory.class);

    private WeatherDao weatherDao;

    public WeatherFactory() {
        weatherDao = new WeatherDao();
    }

    public String getWeather(String city) {
        log.info("load weather");
        try {
            return getWeatherObject(city).toString();
        } catch (IOException | JSONException e) {
            log.error("Incorrect city name", e);
            return "Please enter the correct city name";
        }
    }

    private Weather getWeatherObject(String city) throws IOException, JSONException {
        log.info("load weather");
        return weatherDao.getWeather(city);
    }
}
