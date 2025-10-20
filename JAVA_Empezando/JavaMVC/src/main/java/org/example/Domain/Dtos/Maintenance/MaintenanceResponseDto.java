package org.example.Domain.Dtos.Maintenance;


import org.example.Domain.Dtos.cars.CarResponseDto;
import org.example.utilities.MaintenanceType;

public class MaintenanceResponseDto {
    private Long id;
    private String description;
    private MaintenanceType type;
    private Double cost;
    private String maintenanceDate;
    private CarResponseDto car;
    private String createdAt;
    private String updatedAt;

    public MaintenanceResponseDto() {}

    public MaintenanceResponseDto(Long id, String description, MaintenanceType type, Double cost,
                                  String maintenanceDate, CarResponseDto car,
                                  String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.cost = cost;
        this.maintenanceDate = maintenanceDate;
        this.car = car;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MaintenanceType getType() { return type; }
    public void setType(MaintenanceType type) { this.type = type; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(String maintenanceDate) { this.maintenanceDate = maintenanceDate; }

    public CarResponseDto getCar() { return car; }
    public void setCar(CarResponseDto car) { this.car = car; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
