package com.example.demo4.Controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.core.model.Model;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    
	 @GetMapping("/")
	    public String home(Model model) {
	        //((Object) model).addAttribute("message", "Welcome to the Weather App!");
	        return "index"; 
	    }
	
    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
        
        String geoUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + city;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> geoResponse = restTemplate.getForEntity(geoUrl, String.class);
        
        
        JSONArray geoData = new JSONArray(geoResponse.getBody());
        if (geoData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found");
        }
        JSONObject firstResult = geoData.getJSONObject(0);
        String latitude = firstResult.getString("lat");
        String longitude = firstResult.getString("lon");
        
        
        String weatherUrl = String.format(
            "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m,wind_speed_10m,relative_humidity_2m,pressure_msl&timezone=auto",
            latitude, longitude);
        
        ResponseEntity<String> weatherResponse = restTemplate.getForEntity(weatherUrl, String.class);
        JSONObject weatherData = new JSONObject(weatherResponse.getBody());
        
        
        double temperature = weatherData.getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(0);
        double humidity = weatherData.getJSONObject("hourly").getJSONArray("relative_humidity_2m").getDouble(0);
        double windSpeed = weatherData.getJSONObject("hourly").getJSONArray("wind_speed_10m").getDouble(0);
        double pressure = weatherData.getJSONObject("hourly").getJSONArray("pressure_msl").getDouble(0);
        
        
        String condition;
        if (temperature < 10) {
            condition = "Cold ❄️";
        } else if (temperature < 25) {
            condition = "Cool 🧣";
        } else if (temperature < 29) {
            condition = "Warm 🌞";
        } else {
            condition = "Hot 🔥";
        }
        
       
        Map<String, Object> weatherResult = new HashMap<>();
        weatherResult.put("temperature", temperature);
        weatherResult.put("humidity", humidity);
        weatherResult.put("windSpeed", windSpeed);
        weatherResult.put("pressure", pressure);
        weatherResult.put("condition", condition);
        
        return ResponseEntity.ok(weatherResult);
    }
}
