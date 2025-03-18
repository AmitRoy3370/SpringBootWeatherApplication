package com.example.demo4.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherViewController {
	
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Weather App!");
        return "index"; 
    }

}
