package org.example.controller;

import org.example.models.Car;
import org.example.models.Maintenance;
import org.example.services.MaintenanceService;
import org.example.utilities.MaintenanceType;

import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    /**
     * Create a new Maintenance for a given car.
     *
     * @param description Description of the maintenance
     * @param type Type of maintenance (REPAIR, MOD, ROUTINE)
     * @param cost Cost of the maintenance
     * @param maintenanceDate Date of the maintenance
     * @param car Car associated with this maintenance
     * @return Created Maintenance
     */
    public Maintenance createMaintenance(String description, MaintenanceType type, Double cost,
                                         LocalDateTime maintenanceDate, Car car) {
        return maintenanceService.createMaintenance(description, type, cost, maintenanceDate, car);
    }

    /**
     * Get all maintenances for a specific car.
     *
     * @param car Car object
     * @return List of maintenances
     */
    public List<Maintenance> getMaintenancesByCar(Car car) {
        return maintenanceService.getMaintenancesByCar(car);
    }

    /**
     * Get all maintenances for a specific car by car ID.
     *
     * @param carId Car ID
     * @return List of maintenances
     */
    public List<Maintenance> getMaintenancesByCarId(Long carId) {
        return maintenanceService.getMaintenancesByCarId(carId);
    }

    /**
     * Get a maintenance by its ID.
     *
     * @param id Maintenance ID
     * @return Maintenance object or null if not found
     */
    public Maintenance getMaintenanceById(Long id) {
        return maintenanceService.getMaintenanceById(id);
    }

    /**
     * Get all maintenances of a specific type.
     *
     * @param type Maintenance type
     * @return List of maintenances
     */
    public List<Maintenance> getMaintenancesByType(MaintenanceType type) {
        return maintenanceService.getMaintenancesByType(type);
    }

    /**
     * Update a maintenance.
     *
     * @param id Maintenance ID
     * @param description Updated description
     * @param type Updated type
     * @param cost Updated cost
     * @param maintenanceDate Updated date
     * @return Updated Maintenance
     */
    public Maintenance updateMaintenance(Long id, String description, MaintenanceType type,
                                         Double cost, LocalDateTime maintenanceDate) {
        return maintenanceService.updateMaintenance(id, description, type, cost, maintenanceDate);
    }

    /**
     * Delete a maintenance by its ID.
     *
     * @param id Maintenance ID
     * @return true if deletion succeeded
     */
    public boolean deleteMaintenance(Long id) {
        return maintenanceService.deleteMaintenance(id);
    }

    /**
     * Get all maintenances in the system.
     *
     * @return List of all maintenances
     */
    public List<Maintenance> getAllMaintenances() {
        return maintenanceService.getAllMaintenances();
    }
}
