package org.example.DataAccess.services;

import org.example.Domain.models.Car;
import org.example.Domain.models.Maintenance;

import org.example.utilities.MaintenanceType;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceService {
    private final SessionFactory sessionFactory;

    public MaintenanceService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    // -------------------------
    // CREATE
    // -------------------------
    public Maintenance createMaintenance(String description, MaintenanceType type, Double cost,
                                         LocalDateTime maintenanceDate, Car car) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Car managedCar = session.find(Car.class, car.getId());
            Maintenance maintenance = new Maintenance();
            maintenance.setDescription(description);
            maintenance.setType(type);
            maintenance.setCost(cost);
            maintenance.setMaintenanceDate(maintenanceDate);
            maintenance.setCar(managedCar);

            session.persist(maintenance);
            Hibernate.initialize(managedCar.getOwner());
            tx.commit();
            return maintenance;
        }
    }

    // -------------------------
    // READ
    // -------------------------
    public Maintenance getMaintenanceById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Maintenance maintenance = session.find(Maintenance.class, id);
            if (maintenance != null) {
                Hibernate.initialize(maintenance.getCar());
                Hibernate.initialize(maintenance.getCar().getOwner());
            }
            return maintenance;
        }
    }

    public List<Maintenance> getAllMaintenances() {
        try (Session session = sessionFactory.openSession()) {
            List<Maintenance> maintenances = session.createQuery("FROM Maintenance", Maintenance.class).list();
            // Inicializar las relaciones lazy
            for (Maintenance m : maintenances) {
                Hibernate.initialize(m.getCar());
                Hibernate.initialize(m.getCar().getOwner());
            }
            return maintenances;
        }
    }
    public List<Maintenance> getMaintenancesByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            List<Maintenance> maintenances = session.createQuery(
                            "FROM Maintenance m WHERE m.car.owner.id = :userId ORDER BY m.maintenanceDate DESC",
                            Maintenance.class)
                    .setParameter("userId", userId)
                    .list();

            // Inicializar las relaciones lazy
            for (Maintenance m : maintenances) {
                Hibernate.initialize(m.getCar());
                Hibernate.initialize(m.getCar().getOwner());
            }

            return maintenances;
        } catch (Exception e) {
            String message = String.format("An error occurred when processing: %s. Details: %s",
                    "getMaintenancesByUserId", e);
            System.out.println(message);
            throw e;
        }
    }
    public List<Maintenance> getMaintenancesByCarId(Long carId) {
        try (Session session = sessionFactory.openSession()) {
            List<Maintenance> maintenances = session.createQuery(
                            "FROM Maintenance m WHERE m.car.id = :carId ORDER BY m.maintenanceDate DESC",
                            Maintenance.class)
                    .setParameter("carId", carId)
                    .list();

            // Inicializar las relaciones lazy
            for (Maintenance m : maintenances) {
                Hibernate.initialize(m.getCar());
                Hibernate.initialize(m.getCar().getOwner());
            }
            return maintenances;
        }
    }

    public List<Maintenance> getMaintenancesByType(MaintenanceType type) {
        try (Session session = sessionFactory.openSession()) {
            List<Maintenance> maintenances = session.createQuery(
                            "FROM Maintenance WHERE type = :type",
                            Maintenance.class)
                    .setParameter("type", type)
                    .list();

            // Inicializar las relaciones lazy
            for (Maintenance m : maintenances) {
                Hibernate.initialize(m.getCar());
                Hibernate.initialize(m.getCar().getOwner());
            }
            return maintenances;
        }
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public Maintenance updateMaintenance(Long maintenanceId, String description, MaintenanceType type,
                                         Double cost, LocalDateTime maintenanceDate) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Maintenance maintenance = session.find(Maintenance.class, maintenanceId);
            if (maintenance != null) {
                maintenance.setDescription(description);
                maintenance.setType(type);
                maintenance.setCost(cost);
                maintenance.setMaintenanceDate(maintenanceDate);
                session.merge(maintenance);

                // Inicializar las relaciones lazy antes de cerrar la sesión
                Hibernate.initialize(maintenance.getCar());
                Hibernate.initialize(maintenance.getCar().getOwner());
            }

            tx.commit();
            return maintenance;
        }
    }


    // -------------------------
    // DELETE
    // -------------------------
    public boolean deleteMaintenance(Long maintenanceId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Maintenance maintenance = session.find(Maintenance.class, maintenanceId);
            if (maintenance != null) {
                session.remove(maintenance);
                tx.commit();
                return true;
            }

            tx.rollback();
            return false;
        }
    }
}
