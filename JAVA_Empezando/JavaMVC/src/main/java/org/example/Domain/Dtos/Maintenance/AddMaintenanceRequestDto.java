package org.example.Domain.Dtos.Maintenance;

import org.example.utilities.MaintenanceType;

import java.time.LocalDateTime;

public class AddMaintenanceRequestDto {
    private String description;
    private MaintenanceType type;
    private Double cost;
    private LocalDateTime maintenanceDate;
    private Long carId;

    public AddMaintenanceRequestDto() {}

    public AddMaintenanceRequestDto(String description, MaintenanceType type, Double cost,
                                    LocalDateTime maintenanceDate, Long carId) {
        this.description = description;
        this.type = type;
        this.cost = cost;
        this.maintenanceDate = maintenanceDate;
        this.carId = carId;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MaintenanceType getType() { return type; }
    public void setType(MaintenanceType type) { this.type = type; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public LocalDateTime getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(LocalDateTime maintenanceDate) { this.maintenanceDate = maintenanceDate; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
}
