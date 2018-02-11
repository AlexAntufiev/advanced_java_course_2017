package edu.technopolis.advanced.weather.domain.entity;

public class Weather {
    private String city;
    private double temperature;
    private String description;

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", temperature=" + temperature +
                '}';
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
