package solar_system_monitoring.solar_system_monitoring.exception;

public class SolarEdgeApiException extends RuntimeException {
    public SolarEdgeApiException(String message) {
        super(message);
    }

    public SolarEdgeApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
