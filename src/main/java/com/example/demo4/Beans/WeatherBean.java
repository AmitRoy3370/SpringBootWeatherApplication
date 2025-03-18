package com.example.demo4.Beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo4.Controller.WeatherController;
import org.springframework.http.ResponseEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

import java.util.Map;

@SuppressWarnings("deprecation")
@ManagedBean
@RequestScoped
@ViewScoped
@Component
public class WeatherBean {

    private String city;
    private Map<String, Object> weatherData;

    @Autowired
    private WeatherController weatherController;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Map<String, Object> getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(Map<String, Object> weatherData) {
        this.weatherData = weatherData;
    }

    @SuppressWarnings("unchecked")
	public String getWeather() {
        ResponseEntity<?> response = weatherController.getWeather(city);
        if (response.getStatusCode().is2xxSuccessful()) {
            weatherData = (Map<String, Object>) response.getBody();
            return "index?faces-redirect=true";
        } else {
            weatherData = null; 
            return "index?faces-redirect=true";
        }
    }
}