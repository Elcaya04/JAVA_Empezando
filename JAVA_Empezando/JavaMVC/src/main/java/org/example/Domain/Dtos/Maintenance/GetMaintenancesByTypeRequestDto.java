package org.example.Domain.Dtos.Maintenance;

import org.example.utilities.MaintenanceType;

public class GetMaintenancesByTypeRequestDto {
    private MaintenanceType type;

    public GetMaintenancesByTypeRequestDto() {}
    public GetMaintenancesByTypeRequestDto(MaintenanceType type) { this.type = type; }

    public MaintenanceType getType() { return type; }
    public void setType(MaintenanceType type) { this.type = type; }

}
