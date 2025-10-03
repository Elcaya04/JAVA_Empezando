package org.example.services;

import org.example.models.Car;
import org.example.models.Maintenance;
import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class MaintenanceService {
    private final SessionFactory sessionFactory;

    public MaintenanceService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    // -------------------------
    // CREATE
    // -------------------------
    public Maintenance createMaintenance(Long ID,String Description, String Type, Car ownerCar) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Maintenance maintenance = new Maintenance();
            maintenance.setId(ID);
            maintenance.setDescription(Description);
            maintenance.setOwner(ownerCar);

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
            return session.createQuery("FROM Maintenance ", Maintenance.class).list();
        }
    }

    public List<Maintenance> getMaintenancesByUser(Car user) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Maintenance WHERE owner = :owner", Maintenance.class)
                    .setParameter("owner", user)
                    .list();
        }
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public Maintenance updateMaintenance(Long MaintainanceId,String Description, String Type, Car ownerCar,String make,String model,String year) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Maintenance maintenance = session.find(Maintenance.class, MaintainanceId);
            if (maintenance != null) {
                maintenance.setMake(make);
                maintenance.setModel(model);
                session.merge(maintenance);
            }

            tx.commit();
            return maintenance;
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public boolean deleteCar(Long carId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Car car = session.find(Car.class, carId);
            if (car != null) {
                session.remove(car);
                tx.commit();
                return true;
            }

            tx.rollback();
            return false;
        }
    }
}
