package solar_system_monitoring.solar_system_monitoring.domain.model;

public record PowerData(
    String unit,
    String values,
    String timeUnit,
    String timeUnitQuantity
) {} 