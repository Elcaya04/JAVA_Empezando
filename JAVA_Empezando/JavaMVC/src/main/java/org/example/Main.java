package org.example;

import com.google.gson.Gson;
import org.example.APIController.AuthController;
import org.example.APIController.CarController;
import org.example.APIController.MaintenanceController;
import org.example.DataAccess.HibernateUtil;
import org.example.DataAccess.services.MaintenanceService;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.models.Car;
import org.example.Domain.models.User;
import org.example.DataAccess.services.AuthService;
import org.example.DataAccess.services.CarService;
import org.example.Server.AppServer;
import org.example.utilities.MaintenanceType;

import java.time.LocalDateTime;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var sessionFactory = HibernateUtil.getSessionFactory();

        // Initialize services
        AuthService authService = new AuthService(sessionFactory);
        CarService carService = new CarService(sessionFactory);
        MaintenanceService maintenanceService = new MaintenanceService(sessionFactory);

        // Initialize controllers
        AuthController authController = new AuthController(authService);
        CarController carController = new CarController(carService);
        MaintenanceController maintenanceController = new MaintenanceController(maintenanceService, carService);

        // Auto-create test data if not exists
        initializeTestDataIfNeeded(authService, carService, maintenanceService);

        // Server for request/response (API-like)
        AppServer.initialize(7000,7001);
        Gson gson = new Gson();
        AppServer.getRequestServer().addController(
                authController, requestJson->gson.fromJson(requestJson, RequestDto.class),
                responseObj-> gson.toJson(responseObj)
        );
        AppServer.getRequestServer().addController(
                carController, requestJson->gson.fromJson(requestJson,RequestDto.class),
                responseObj-> gson.toJson(responseObj)
        );
        AppServer.getRequestServer().addController(
                maintenanceController, requestJson->gson.fromJson(requestJson,RequestDto.class),
                responseObj-> gson.toJson(responseObj)
        );
AppServer.getRequestServer().start();
AppServer.getMessageBroadcaster().start();
AppServer.getRequestServer().broadcast("Server Iniciado");
    }

    /**
     * Verifica si existen datos de prueba en la BD y los crea solo si no existen
     */
    private static void initializeTestDataIfNeeded(AuthService authService,
                                                   CarService carService,
                                                   MaintenanceService maintenanceService) {
        try {
            // Verificar si ya existen usuarios
            User existingUser = authService.getUserByUsername("johndoe");

            if (existingUser != null) {
                System.out.println("✓ Test data already exists in database");
                System.out.println("  Users: johndoe, janedoe, admin");

                // Contar carros y mantenimientos existentes
                var allCars = carService.getAllCars();
                var allMaintenances = maintenanceService.getAllMaintenances();

                System.out.println("  Cars: " + allCars.size());
                System.out.println("  Maintenances: " + allMaintenances.size());
                System.out.println();
                return;
            }

            // Si no existen, crear datos de prueba
            System.out.println("\n========== Creating Test Data ==========");

            // Create users
            User user1 = authService.register("johndoe", "john@example.com", "pass123", "USER");
            User user2 = authService.register("janedoe", "jane@example.com", "pass456", "USER");
            User admin = authService.register("admin", "admin@example.com", "admin123", "ADMIN");
            System.out.println("✓ Created 3 users");

            // Create cars
            Car car1 = carService.createCar("Toyota", "86", 2022, user1.getId());
            Car car2 = carService.createCar("Subaru", "BRZ", 2023, user1.getId());
            Car car3 = carService.createCar("Honda", "Civic Type R", 2024, user2.getId());
            Car car4 = carService.createCar("Mazda", "MX-5 Miata", 2021, user2.getId());
            System.out.println("✓ Created 4 cars");

            // Create maintenances
            createMaintenance(maintenanceService, "Oil change and filter replacement",
                    MaintenanceType.ROUTINE, 75.50, 30, car1);
            createMaintenance(maintenanceService, "New brake pads installed",
                    MaintenanceType.REPAIR, 250.00, 15, car1);
            createMaintenance(maintenanceService, "Cold air intake installation",
                    MaintenanceType.MOD, 350.00, 10, car1);
            createMaintenance(maintenanceService, "Tire rotation and alignment",
                    MaintenanceType.ROUTINE, 120.00, 20, car2);
            createMaintenance(maintenanceService, "Turbo upgrade kit",
                    MaintenanceType.MOD, 1500.00, 5, car3);
            createMaintenance(maintenanceService, "Transmission fluid replacement",
                    MaintenanceType.ROUTINE, 180.00, 25, car3);
            createMaintenance(maintenanceService, "Suspension repair - coilovers",
                    MaintenanceType.REPAIR, 450.00, 8, car4);
            createMaintenance(maintenanceService, "Performance exhaust system",
                    MaintenanceType.MOD, 800.00, 3, car4);

            System.out.println("✓ Created 8 maintenances");

            // Display summary
            System.out.println("\n========== Test Data Summary ==========");
            System.out.println("Users:");
            System.out.println("  - johndoe (2 cars: Toyota 86, Subaru BRZ)");
            System.out.println("  - janedoe (2 cars: Honda Civic Type R, Mazda MX-5)");
            System.out.println("  - admin (no cars)");

            System.out.println("\nCars with Maintenances:");
            System.out.println("  - Toyota 86: 3 maintenances (1 ROUTINE, 1 REPAIR, 1 MOD)");
            System.out.println("  - Subaru BRZ: 1 maintenance (ROUTINE)");
            System.out.println("  - Honda Civic Type R: 2 maintenances (1 ROUTINE, 1 MOD)");
            System.out.println("  - Mazda MX-5: 2 maintenances (1 REPAIR, 1 MOD)");
            System.out.println("=======================================\n");

        } catch (Exception e) {
            System.err.println("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method para crear mantenimientos con menos repetición de código
     */
    private static void createMaintenance(MaintenanceService service, String description,
                                          MaintenanceType type, Double cost,
                                          int daysAgo, Car car) {
        service.createMaintenance(
                description,
                type,
                cost,
                LocalDateTime.now().minusDays(daysAgo),
                car
        );
    }
}