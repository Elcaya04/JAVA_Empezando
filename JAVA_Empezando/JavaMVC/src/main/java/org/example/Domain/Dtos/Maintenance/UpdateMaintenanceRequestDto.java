package org.example.Domain.Dtos.Maintenance;

import org.example.utilities.MaintenanceType;

import java.time.LocalDateTime;

public class UpdateMaintenanceRequestDto {
    private Long id;
    private String description;
    private MaintenanceType type;
    private Double cost;
    private LocalDateTime maintenanceDate; // ← Cambiado de String a LocalDateTime

    public UpdateMaintenanceRequestDto() {}

    public UpdateMaintenanceRequestDto(Long id, String description, MaintenanceType type,
                                       Double cost, LocalDateTime maintenanceDate) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.cost = cost;
        this.maintenanceDate = maintenanceDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MaintenanceType getType() { return type; }
    public void setType(MaintenanceType type) { this.type = type; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public LocalDateTime getMaintenanceDate() { return maintenanceDate; } // ← Cambiado
    public void setMaintenanceDate(LocalDateTime maintenanceDate) { this.maintenanceDate = maintenanceDate; }
}
