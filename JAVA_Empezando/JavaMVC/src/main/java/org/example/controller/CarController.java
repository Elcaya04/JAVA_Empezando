package org.example.controller;

import org.example.models.Car;
import org.example.models.User;
import org.example.services.CarService;

import java.util.List;

public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Create a new car for a given user.
     *
     * @param make  Car make (e.g., Toyota)
     * @param model Car model (e.g., 86)
     * @param year  Year of the car
     * @param owner Owner of the car
     * @return Created Car
     */
    public Car createCar(String make, String model, int year, User owner) {
        return carService.createCar(make, model, year, owner);
    }

    /**
     * Get all cars owned by a specific user.
     *
     * @param owner User
     * @return List of cars
     */
    public List<Car> getCarsByUser(User owner) {
        return carService.getCarsByUser(owner);
    }

    /**
     * Get a car by its ID.
     *
     * @param id Car ID
     * @return Car object or null if not found
     */
    public Car getCarById(Long id) {
        return carService.getCarById(id);
    }

    /**
     * Update a car.
     *
     * @param car Updated car object
     * @return Updated car
     */
    public Car updateCar(Car car) {
        return carService.updateCar(car.getId(), car.getMake(), car.getModel(), car.getYear());
    }

    /**
     * Delete a car by its ID.
     *
     * @param id Car ID
     * @return true if deletion succeeded
     */
    public boolean deleteCar(Long id) {
        return carService.deleteCar(id);
    }
}
