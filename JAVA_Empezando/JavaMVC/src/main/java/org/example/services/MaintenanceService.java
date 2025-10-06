package org.example.services;

import org.example.models.Car;
import org.example.models.Maintenance;

import org.example.utilities.MaintenanceType;
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

            Maintenance maintenance = new Maintenance();
            maintenance.setDescription(description);
            maintenance.setType(type);
            maintenance.setCost(cost);
            maintenance.setMaintenanceDate(maintenanceDate);
            maintenance.setCar(car);

            session.persist(maintenance);
            tx.commit();
            return maintenance;
        }
    }

    // -------------------------
    // READ
    // -------------------------
    public Maintenance getMaintenanceById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Maintenance.class, id);
        }
    }

    public List<Maintenance> getAllMaintenances() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Maintenance", Maintenance.class).list();
        }
    }

    public List<Maintenance> getMaintenancesByCar(Car car) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Maintenance WHERE car = :car ORDER BY maintenanceDate DESC", Maintenance.class)
                    .setParameter("car", car)
                    .list();
        }
    }

    public List<Maintenance> getMaintenancesByCarId(Long carId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Maintenance m WHERE m.car.id = :carId ORDER BY m.maintenanceDate DESC", Maintenance.class)
                    .setParameter("carId", carId)
                    .list();
        }
    }

    public List<Maintenance> getMaintenancesByType(MaintenanceType type) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Maintenance WHERE type = :type", Maintenance.class)
                    .setParameter("type", type)
                    .list();
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
