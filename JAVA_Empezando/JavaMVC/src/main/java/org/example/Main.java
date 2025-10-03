package org.example;

import org.example.controller.AuthController;
import org.example.controller.CarController;
import org.example.models.Car;
import org.example.models.User;
import org.example.services.AuthService;
import org.example.services.CarService;

import org.example.utilities.HibernateUtil;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CarService carService = new CarService(HibernateUtil.getSessionFactory());
        CarController carController = new CarController(carService);

        AuthService authService = new AuthService(HibernateUtil.getSessionFactory());
        AuthController authController = new AuthController(authService);

        try {
            User user = authController.register("johndoe", "john@example.com", "password123", "USER");

            // Create a car
            Car car = carController.createCar("Toyota", "86", 2022, user);
            System.out.println("Created car: " + car.getMake() + " " + car.getModel());

            // List cars for the user
            carController.getCarsByUser(user).forEach(c ->
                    System.out.println("Car: " + c.getYear() + " " + c.getMake() + " " + c.getModel())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
