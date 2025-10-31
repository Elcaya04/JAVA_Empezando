package org.example.APIController;

import basecontroller.IBaseController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.DataAccess.services.CarService;
import org.example.Domain.Dtos.Auth.UserResponseDto;
import org.example.Domain.Dtos.Maintenance.*;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.cars.CarResponseDto;
import org.example.Domain.models.Car;
import org.example.Domain.models.Maintenance;
import org.example.DataAccess.services.MaintenanceService;
import org.example.utilities.LocalDateTimeAdapter;
import org.example.utilities.MaintenanceType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MaintenanceController implements IBaseController<ResponseDto,RequestDto> {
    private final MaintenanceService maintenanceService;
    private final CarService carService;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public MaintenanceController(MaintenanceService maintenanceService, CarService carService) {
        this.maintenanceService = maintenanceService;
        this.carService = carService;
    }

    @Override
    public String getControllerName() {
        return "Maintenance";
    }

    @Override
    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "add":
                    return handleAddMaintenance(request);
                case "update":
                    return handleUpdateMaintenance(request);
                case "delete":
                    return handleDeleteMaintenance(request);
                case "list":
                    return handleListMaintenances(request);
                case "listByCar":
                    return handleListMaintenancesByCar(request);
                case "get":
                    return handleGetMaintenance(request);
                case "listByType":
                    return handleListMaintenancesByType(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    // --- ADD MAINTENANCE ---
    private ResponseDto handleAddMaintenance(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            AddMaintenanceRequestDto dto = gson.fromJson(request.getData(), AddMaintenanceRequestDto.class);

            // Get the car first
            Car car = carService.getCarById(dto.getCarId());
            if (car == null) {
                return new ResponseDto(false, "Car not found", null);
            }

            Maintenance maintenance = maintenanceService.createMaintenance(
                    dto.getDescription(),
                    dto.getType(),
                    dto.getCost(),
                    dto.getMaintenanceDate(),
                    car
            );

            MaintenanceResponseDto response = toResponseDto(maintenance);
            return new ResponseDto(true, "Maintenance added successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleAddMaintenance: " + e.getMessage());
            throw e;
        }
    }

    // --- UPDATE MAINTENANCE ---
    private ResponseDto handleUpdateMaintenance(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            UpdateMaintenanceRequestDto dto = gson.fromJson(request.getData(), UpdateMaintenanceRequestDto.class);
            Maintenance updated = maintenanceService.updateMaintenance(
                    dto.getId(),
                    dto.getDescription(),
                    dto.getType(),
                    dto.getCost(),
                    dto.getMaintenanceDate()
            );

            if (updated == null) {
                return new ResponseDto(false, "Maintenance not found", null);
            }

            MaintenanceResponseDto response = toResponseDto(updated);
            return new ResponseDto(true, "Maintenance updated successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleUpdateMaintenance: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // --- DELETE MAINTENANCE ---
    private ResponseDto handleDeleteMaintenance(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            DeleteMaintenanceRequestDto dto = gson.fromJson(request.getData(), DeleteMaintenanceRequestDto.class);
            boolean deleted = maintenanceService.deleteMaintenance(dto.getId());

            if (!deleted) {
                return new ResponseDto(false, "Maintenance not found or could not be deleted", null);
            }

            return new ResponseDto(true, "Maintenance deleted successfully", null);
        } catch (Exception e) {
            System.out.println("Error in handleDeleteMaintenance: " + e.getMessage());
            throw e;
        }
    }

    // --- LIST ALL MAINTENANCES ---
    private ResponseDto handleListMaintenances(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }
            Long userId = Long.parseLong(request.getToken());
            List<Maintenance> maintenances = maintenanceService.getMaintenancesByUserId(userId);

            List<MaintenanceResponseDto> maintenanceDtos = maintenances.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListMaintenancesResponseDto response = new ListMaintenancesResponseDto(maintenanceDtos);
            return new ResponseDto(true, "Maintenances retrieved successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleListMaintenances: " + e.getMessage());
            throw e;
        }
    }

    // --- LIST MAINTENANCES BY CAR ---
    private ResponseDto handleListMaintenancesByCar(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            GetMaintenancesByCarRequestDto dto = gson.fromJson(request.getData(), GetMaintenancesByCarRequestDto.class);
            List<Maintenance> maintenances = maintenanceService.getMaintenancesByCarId(dto.getCarId());

            List<MaintenanceResponseDto> maintenanceDtos = maintenances.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListMaintenancesResponseDto response = new ListMaintenancesResponseDto(maintenanceDtos);
            return new ResponseDto(true, "Maintenances for car retrieved successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleListMaintenancesByCar: " + e.getMessage());
            throw e;
        }
    }

    // --- GET SINGLE MAINTENANCE ---
    private ResponseDto handleGetMaintenance(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            DeleteMaintenanceRequestDto dto = gson.fromJson(request.getData(), DeleteMaintenanceRequestDto.class);
            Maintenance maintenance = maintenanceService.getMaintenanceById(dto.getId());

            if (maintenance == null) {
                return new ResponseDto(false, "Maintenance not found", null);
            }

            MaintenanceResponseDto response = toResponseDto(maintenance);
            return new ResponseDto(true, "Maintenance retrieved successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleGetMaintenance: " + e.getMessage());
            throw e;
        }
    }

    // --- LIST MAINTENANCES BY TYPE ---
    private ResponseDto handleListMaintenancesByType(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            GetMaintenancesByTypeRequestDto dto = gson.fromJson(request.getData(), GetMaintenancesByTypeRequestDto.class);
            List<Maintenance> maintenances = maintenanceService.getMaintenancesByType(dto.getType());

            List<MaintenanceResponseDto> maintenanceDtos = maintenances.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListMaintenancesResponseDto response = new ListMaintenancesResponseDto(maintenanceDtos);
            return new ResponseDto(true, "Maintenances by type retrieved successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleListMaintenancesByType: " + e.getMessage());
            throw e;
        }
    }

    // --- Helper method ---
    private MaintenanceResponseDto toResponseDto(Maintenance maintenance) {
        var car = maintenance.getCar();

        // Inicializar el owner dentro de la sesión (si es necesario)
        var owner = car.getOwner();


        var ownerDto = new UserResponseDto(
                owner.getId(),
                owner.getUsername(),
                owner.getEmail(),
                owner.getRole(),
                owner.getCreatedAt().toString(),
                owner.getUpdatedAt().toString()
        );

        var carDto = new CarResponseDto(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                ownerDto,
                car.getCreatedAt().toString(),
                car.getUpdatedAt().toString()
        );

        return new MaintenanceResponseDto(
                maintenance.getId(),
                maintenance.getDescription(),
                maintenance.getType(),
                maintenance.getCost(),
                maintenance.getMaintenanceDate().toString(),
                carDto,
                maintenance.getCreatedAt().toString(),
                maintenance.getUpdatedAt().toString()
        );
    }
}
