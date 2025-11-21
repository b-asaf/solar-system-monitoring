package solar_system_monitoring.solar_system_monitoring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import solar_system_monitoring.solar_system_monitoring.client.SolarEdgeApiClient;
import solar_system_monitoring.solar_system_monitoring.exception.SolarEdgeApiException;

@Slf4j
@Service
public class SolarEdgeService {
    private final SolarEdgeApiClient solarEdgeApiClient;
    private final ObjectMapper objectMapper;

    public SolarEdgeService(
            SolarEdgeApiClient solarEdgeApiClient,
            ObjectMapper objectMapper) {
        this.solarEdgeApiClient = solarEdgeApiClient;
        this.objectMapper = objectMapper;
        log.info("SolarEdgeService initialized");
    }

    public Mono<Boolean> healthCheck() {
        log.info("Performing solarEdge API health check");
        return solarEdgeApiClient.getSiteOverview()
                .map(response -> {
                    log.info("Health check passed");
                    return true;
                })
                .doOnError(error -> {
                    log.error("Health check failed: {}", error.getMessage());
                })
                .onErrorReturn(false);
    }

    public Mono<String> fetchSiteOverview() {
        log.info("Fetching site overview from solarEdge API");

        return solarEdgeApiClient.getSiteOverview()
                .map(this::validateAndLogResponse)
                .doOnSuccess(data -> {
                    log.info("Successfully fetch site overview");
                })
                .doOnError(error -> {
                    log.error("Error fetching site overview: {}", error.getMessage(), error);
                });
    }

    private String validateAndLogResponse(String response) {
        try {
            log.debug("Validating JSON response");
            JsonNode root = objectMapper.readTree(response);

            if (root.has("error")) {
                String error = root.path("error").asText();
                log.error("API returned error: {}", error);
                throw new SolarEdgeApiException("SolarEdge API error:" + error);
            }

            if (root.isEmpty()) {
                log.warn("Empty response from SolarEdge API");
                throw new SolarEdgeApiException("Empty response from SolarEdge API");
            }

            return response;
        } catch (Exception e) {
            log.error("Response validation failed: {}", e.getMessage(), e);
            throw new SolarEdgeApiException("Response validation failed", e);
        }
    }

}