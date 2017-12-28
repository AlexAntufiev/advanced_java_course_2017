package edu.technopolis.advanced.weather;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Weather {
    private static final Logger log = LoggerFactory.getLogger(Weather.class);


    private static final String BASE_URL = "api.openweathermap.org/data/2.5/weather?q=";
    private String city;
    private int temperature;

    Weather(String city) {
        this.city = city;
    }

    private JSONObject getResponse(String city) throws IOException, JSONException {
        final String url = BASE_URL + city;
        log.info("url = " + url);
        return JsonReader.read(url);
    }

    public String getWeather() {
        try {
            JSONObject response = getResponse(this.city);
            this.temperature = response.getJSONObject("main").getInt("temp") - 273;
            return this.toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return "Please enter the correct city name";
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", temperature=" + temperature +
                '}';
    }

}
