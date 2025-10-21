package org.example.Domain.Dtos.Maintenance;

public class GetMaintenancesByCarRequestDto {
    private Long carId;

    public GetMaintenancesByCarRequestDto() {}
    public GetMaintenancesByCarRequestDto(Long carId) { this.carId = carId; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
}

