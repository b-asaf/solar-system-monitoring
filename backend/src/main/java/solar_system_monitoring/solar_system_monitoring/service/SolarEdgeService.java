package solar_system_monitoring.solar_system_monitoring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import solar_system_monitoring.solar_system_monitoring.domain.model.SiteData;
import solar_system_monitoring.solar_system_monitoring.domain.model.SiteOverview;
import solar_system_monitoring.solar_system_monitoring.domain.model.PowerData;

@Service
public class SolarEdgeService {
    private final RestTemplate restTemplate;
    private final String siteId;
    private final String apiKey;
    private final String apiUrl;
    private final boolean isDev;

    public SolarEdgeService(
            @Value("${solaredge.api.url:}") String apiUrl,
            @Value("${solaredge.site.id:}") String siteId,
            @Value("${solaredge.api.key:}") String apiKey,
            @Value("${spring.profiles.active:}") String activeProfile) {
        this.restTemplate = new RestTemplate();
        this.apiUrl = apiUrl;
        this.siteId = siteId;
        this.apiKey = apiKey;
        this.isDev = activeProfile.contains("dev");
    }

    public SiteData getSiteData() {
        if (isDev) {
            return mockSiteData();
        }
        String url = String.format("%s/site/%s/overview?api_key=%s", apiUrl, siteId, apiKey);
        return restTemplate.getForObject(url, SiteData.class);
    }

    public SiteData getSitePower() {
        if (isDev) {
            return mockSiteData();
        }
        String url = String.format("%s/site/%s/power?api_key=%s", apiUrl, siteId, apiKey);
        return restTemplate.getForObject(url, SiteData.class);
    }

    private SiteData mockSiteData() {
        SiteOverview overview = new SiteOverview(
            "dev-site-id",
            "Dev Solar Site",
            "dev-account-id",
            "active",
            "10kW",
            "2024-06-21T12:00:00Z",
            "2023-01-01",
            "2023-02-01",
            "Development mock site",
            "residential",
            "MonoPERC",
            "http://localhost",
            "public"
        );
        PowerData power = new PowerData(
            "kW",
            "[1,2,3,4,5]",
            "HOUR",
            "5"
        );
        return new SiteData(overview, power);
    }
} 