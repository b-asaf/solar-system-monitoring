package solar_system_monitoring.solar_system_monitoring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;
import solar_system_monitoring.solar_system_monitoring.service.SolarEdgeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/solaredge")
@RequiredArgsConstructor
public class SolarEdgeController {
    private final SolarEdgeService solarEdgeService;

    @GetMapping("/overview")
    @Operation(summary = "Get site overview", description = "Fetch site overview data from SolarEdge API")
    public Mono<String> getSiteOverview() {
        return solarEdgeService.fetchSiteOverview();
    }

    // @GetMapping("/site/power")
    // public SiteData getSitePower() {
    // return solarEdgeService.getSitePower();
    // }
}