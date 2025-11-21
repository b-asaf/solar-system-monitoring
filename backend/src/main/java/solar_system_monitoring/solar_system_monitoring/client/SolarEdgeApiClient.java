package solar_system_monitoring.solar_system_monitoring.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import solar_system_monitoring.solar_system_monitoring.config.SolarEdgeConfig;
import solar_system_monitoring.solar_system_monitoring.exception.SolarEdgeApiException;

@Component
public class SolarEdgeApiClient {

        private final WebClient webClient;
        private final SolarEdgeConfig solarEdgeConfig;

        public SolarEdgeApiClient(
                        WebClient.Builder webClientBuilder,
                        SolarEdgeConfig solarEdgeConfig) {
                this.solarEdgeConfig = solarEdgeConfig;
                this.webClient = webClientBuilder
                                .baseUrl(solarEdgeConfig.getBaseUrl())
                                .build();
        }

        public Mono<String> getSiteOverview() {
                return webClient
                                .get()
                                .uri("/site/{siteId}/overview?api_key={apiKey}",
                                                solarEdgeConfig.getSiteId(),
                                                solarEdgeConfig.getApiKey())
                                .retrieve()
                                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                                                .flatMap(body -> Mono
                                                                .error(new SolarEdgeApiException("Error: " + body))))
                                .bodyToMono(String.class);
        }

        public Mono<String> getSitePowerDetails(String startDate, String endDate) {
                return webClient
                                .get()
                                .uri("/site/{siteId}/powerDetails?startDate={startDate}&endDate={endDate}&api_key={apiKey}",
                                                solarEdgeConfig.getSiteId(), startDate, endDate,
                                                solarEdgeConfig.getApiKey())
                                .retrieve()
                                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                                                .flatMap(body -> Mono
                                                                .error(new SolarEdgeApiException("Error: " + body))))
                                .bodyToMono(String.class);
        }

        public Mono<String> getEnergyDetails(String startDate, String endDate) {
                return webClient
                                .get()
                                .uri("/site/{siteId}/energyDetails?startDate={startDate}&endDate={endDate}&timeUnit=DAY&api_key={apiKey}",
                                                solarEdgeConfig.getSiteId(), startDate, endDate,
                                                solarEdgeConfig.getApiKey())
                                .retrieve()
                                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                                                .flatMap(body -> Mono
                                                                .error(new SolarEdgeApiException("Error: " + body))))
                                .bodyToMono(String.class);
        }
}