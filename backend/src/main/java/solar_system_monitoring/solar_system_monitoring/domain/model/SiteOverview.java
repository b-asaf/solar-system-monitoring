package solar_system_monitoring.solar_system_monitoring.domain.model;

public record SiteOverview(
    String id,
    String name,
    String accountId,
    String status,
    String peakPower,
    String lastUpdateTime,
    String installationDate,
    String ptoDate,
    String notes,
    String type,
    String primaryModule,
    String urls,
    String publicSettings
) {} 