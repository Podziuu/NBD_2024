package managers;

import exceptions.LogicException;
import exceptions.ParameterException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.Client;
import models.Item;
import models.Rent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RentManager {

    private EntityManagerFactory entityManagerFactory;

    public RentManager() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    public Rent rentItem(String rentId, LocalDateTime beginTime, Client client, Item item) {
        Rent rent = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            rent = new Rent(rentId, beginTime, client, item);
            entityManager.persist(rent);

            entityManager.getTransaction().commit();
        } catch (ParameterException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error renting item: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }

        return rent;
    }

    public void returnItem(Item item) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Rent rent = entityManager.createQuery("SELECT r FROM Rent r WHERE r.item = :item AND r.endTime IS NULL", Rent.class)
                    .setParameter("item", item)
                    .getSingleResult();

            rent.endRent(LocalDateTime.now());
            rent.setArchive(true);

            entityManager.merge(rent);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error returning item: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    public List<Rent> getAllClientRents(Client client) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Rent> clientRents;

        try {
            clientRents = entityManager.createQuery("SELECT r FROM Rent r WHERE r.client = :client", Rent.class)
                    .setParameter("client", client)
                    .getResultList();
        } finally {
            entityManager.close();
        }

        return clientRents;
    }

    public List<Rent> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Rent> rents;

        try {
            rents = entityManager.createQuery("SELECT r FROM Rent r", Rent.class).getResultList();
        } finally {
            entityManager.close();
        }

        return rents;
    }

    public Optional<Rent> find(String rentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Optional<Rent> rent;

        try {
            rent = Optional.ofNullable(entityManager.createQuery("SELECT r FROM Rent r WHERE r.rentId = :rentId", Rent.class)
                    .setParameter("rentId", rentId)
                    .getSingleResult());
        } catch (Exception e) {
            rent = Optional.empty();
        } finally {
            entityManager.close();
        }

        return rent;
    }

    public String report() {
        StringBuilder report = new StringBuilder();
        List<Rent> rents = getAll();

        for (Rent rent : rents) {
            report.append(rent.getRentInfo()).append("\n");
        }

        return report.toString();
    }

    public List<Rent> getAllArchiveRents() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Rent> archivedRents;

        try {
            archivedRents = entityManager.createQuery("SELECT r FROM Rent r WHERE r.archive = true", Rent.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }

        return archivedRents;
    }
}