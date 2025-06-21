package solar_system_monitoring.solar_system_monitoring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public String home() {
        return "Welcome to the Solar System Monitoring API!";
    }
} 