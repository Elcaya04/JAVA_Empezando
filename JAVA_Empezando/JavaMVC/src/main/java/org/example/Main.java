package org.example;

import org.example.APIController.AuthController;
import org.example.APIController.CarController;
import org.example.APIController.MaintenanceController;
import org.example.DataAccess.HibernateUtil;
import org.example.DataAccess.services.MaintenanceService;
import org.example.Domain.models.Car;
import org.example.Domain.models.User;
import org.example.DataAccess.services.AuthService;
import org.example.DataAccess.services.CarService;
import org.example.Server.MessageBroadcaster;
import org.example.Server.SocketServer;
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

        // Create test data
        var createTestData = true;
        if (createTestData) {
            System.out.println("\n========== Creating Test Data ==========");

            try {
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
                maintenanceService.createMaintenance(
                        "Oil change and filter replacement",
                        MaintenanceType.ROUTINE,
                        75.50,
                        LocalDateTime.now().minusDays(30),
                        car1
                );

                maintenanceService.createMaintenance(
                        "New brake pads installed",
                        MaintenanceType.REPAIR,
                        250.00,
                        LocalDateTime.now().minusDays(15),
                        car1
                );

                maintenanceService.createMaintenance(
                        "Cold air intake installation",
                        MaintenanceType.MOD,
                        350.00,
                        LocalDateTime.now().minusDays(10),
                        car1
                );

                maintenanceService.createMaintenance(
                        "Tire rotation and alignment",
                        MaintenanceType.ROUTINE,
                        120.00,
                        LocalDateTime.now().minusDays(20),
                        car2
                );

                maintenanceService.createMaintenance(
                        "Turbo upgrade kit",
                        MaintenanceType.MOD,
                        1500.00,
                        LocalDateTime.now().minusDays(5),
                        car3
                );

                maintenanceService.createMaintenance(
                        "Transmission fluid replacement",
                        MaintenanceType.ROUTINE,
                        180.00,
                        LocalDateTime.now().minusDays(25),
                        car3
                );

                maintenanceService.createMaintenance(
                        "Suspension repair - coilovers",
                        MaintenanceType.REPAIR,
                        450.00,
                        LocalDateTime.now().minusDays(8),
                        car4
                );

                maintenanceService.createMaintenance(
                        "Performance exhaust system",
                        MaintenanceType.MOD,
                        800.00,
                        LocalDateTime.now().minusDays(3),
                        car4
                );

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
                System.err.println("Error creating test data: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Server for request/response (API-like)
        int requestPort = 7000;
        SocketServer requestServer = new SocketServer(
                requestPort,
                authController,
                carController,
                maintenanceController);

        // Server for chat/broadcasting (persistent connections)
        int messagePort = 7001;
        MessageBroadcaster messageBroadcaster = new MessageBroadcaster(messagePort, requestServer);

        // Register the broadcaster with the request server so it can broadcast messages
        requestServer.setMessageBroadcaster(messageBroadcaster);

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n========== Shutting down servers ==========");
            requestServer.stop();
            messageBroadcaster.stop();
            System.out.println("Servers stopped successfully");
        }));

        // Start servers
        requestServer.start();
        messageBroadcaster.start();

        System.out.println("\n========== Servers Started ==========");
        System.out.println("Request Server: localhost:" + requestPort);
        System.out.println("Message Broadcaster: localhost:" + messagePort);
        System.out.println("=====================================");
        System.out.println("Server is running. Press Ctrl+C to stop.\n");
    }
    }

