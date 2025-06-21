package solar_system_monitoring.solar_system_monitoring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solar_system_monitoring.solar_system_monitoring.domain.model.SiteData;
import solar_system_monitoring.solar_system_monitoring.service.SolarEdgeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/solaredge")
@RequiredArgsConstructor
public class SolarEdgeController {
    private final SolarEdgeService solarEdgeService;

    @GetMapping("/site")
    public SiteData getSiteData() {
        return solarEdgeService.getSiteData();
    }

    @GetMapping("/site/power")
    public SiteData getSitePower() {
        return solarEdgeService.getSitePower();
    }
} 