package repos;

import exceptions.LogicException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import models.Rent;

import java.util.ArrayList;
import java.util.List;

public class RentRepository implements Repository<Rent> {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void create(Rent rent) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(rent);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public Rent get(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT r FROM Rent r WHERE r.rentId = :id", Rent.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
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
            Rent managedRent = entityManager.find(Rent.class, rent.getRentId());
            if (managedRent != null) {
                entityManager.remove(managedRent);
            }
            entityManager.getTransaction().commit();
        }
    }

    public List<Rent> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT r FROM Rent r", Rent.class)
                    .getResultList();
        }
    }

    public Rent find(String rentId) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT r FROM Rent r WHERE r.rentId = :rentId", Rent.class)
                    .setParameter("rentId", rentId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
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
}
