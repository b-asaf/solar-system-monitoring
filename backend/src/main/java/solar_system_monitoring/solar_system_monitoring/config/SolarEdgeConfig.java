package solar_system_monitoring.solar_system_monitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "solaredge.api")
@Data
public class SolarEdgeConfig {
    private String baseUrl;
    private String version;
    private String apiKey;
    private String siteId;
} 