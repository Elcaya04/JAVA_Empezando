package org.example.controller;

import org.example.models.Car;
import org.example.models.Maintenance;
import org.example.services.MaintenanceService;

import java.util.List;

public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    /**
     * Create a new Maintenance for a given car.
     *
     * @param ID
     * @param Description
     * @param Type
     * @param ownerCar
     * @return Created Car
     */
    public Maintenance createMaintenance(Long ID, String Description, String Type, Car ownerCar) {
        return maintenanceService.createMaintenance(ID,Description,Type, ownerCar);
    }

    /**
     * Get all cars owned by a specific user.
     *
     * @param owner User
     * @return List of cars
     */
    public List<Maintenance> getMaintencanceByCar(Car owner) {
        return maintenanceService.getMaintenancesByUser(owner);

    /**
     * Get a Maintenance by its ID.
     *
     * @param id Maintenance ID
     * @return Maintenance object or null if not found
     */
    public Maintenance getMaintenanceById(Long id) {
        return maintenanceService.getMaintenanceById(id);
    }

    /**
     * Update a Maintenance.
     *
     * @param maintenance Updated Maintenance object
     * @return Updated Maintenance
     */
    public Maintenance updateMaintenance(Maintenance maintenance) {
        return maintenanceService.updateMaintenance{maintenance.getId(), maintenance.getMake(), maintenance.getModel(), maintenance.getYear());
    }

    /**
     * Delete a car by its ID.
     *
     * @param id Car ID
     * @return true if deletion succeeded
     */
    public boolean deleteMaintenance(Long id) {
        return maintenanceService.deleteCar(id);
    }
}
    }}