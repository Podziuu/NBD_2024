package repos;

import exceptions.LogicException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import models.Rent;
import models.Client;

import java.util.ArrayList;
import java.util.List;

public class RentRepository implements Repository<Rent> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public long create(Rent rent) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(rent);
            entityManager.getTransaction().commit();
            return rent.getId();
        }
    }

    @Override
    public Rent get(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(Rent.class, id);
        }
    }

    @Override
    public void update(Rent rent) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(rent);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(Rent rent) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Rent managedRent = entityManager.find(Rent.class, rent.getId());
            if (managedRent != null) {
                entityManager.remove(managedRent);
            }
            entityManager.getTransaction().commit();
        }
    }

    public List<Rent> getActiveRentsByClient(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT r FROM Rent r WHERE r.client = :client AND r.endTime IS NULL", Rent.class)
                    .setParameter("client", client)
                    .getResultList();
        }
    }

    public List<Rent> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT r FROM Rent r", Rent.class)
                    .getResultList();
        }
    }

    public String report() {
        StringBuilder report = new StringBuilder();
        List<Rent> rents = getAll();
        for (Rent rent : rents) {
            report.append(rent.getRentInfo()).append("\n");
        }
        return report.toString();
    }

    public int getSize() {
        return getAll().size();
    }

    public long countActiveRentsByClient(Client client) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            String query = "SELECT COUNT(r) FROM Rent r WHERE r.client = :client AND r.endTime IS NULL";
            return entityManager.createQuery(query, Long.class)
                    .setParameter("client", client)
                    .getSingleResult();
        }
    }
}
