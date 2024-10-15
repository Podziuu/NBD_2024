package managers;

import exceptions.LogicException;
import exceptions.ParameterException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import models.*;
import repos.RentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RentManager {

    private EntityManagerFactory entityManagerFactory;
    private RentRepository rentRepository;

    public RentManager() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
        this.rentRepository = new RentRepository();
    }
    
    public Rent rentItem(LocalDateTime beginTime, Client client, Item item) throws ParameterException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();

            Client managedClient = entityManager.find(Client.class, client.getPersonalId(), LockModeType.PESSIMISTIC_WRITE);

            int maxRentals = getMaxRentalsForClient(managedClient.getClientType());
            long activeRents = countActiveRentsByClient(managedClient, entityManager);

            if (activeRents >= maxRentals) {
                throw new ParameterException("Client has reached the maximum number of active rentals!");
            }

            Item managedItem = entityManager.find(Item.class, item.getId(), LockModeType.PESSIMISTIC_WRITE);

            if (!managedItem.isAvailable()) {
                throw new ParameterException("Item is already rented!");
            }

            Rent rent = new Rent(beginTime, managedClient, managedItem);
            rentRepository.create(rent);

            entityManager.getTransaction().commit();
            return rent;
        }
    }

    public void endRent(long rentId, LocalDateTime endTime) {
        Rent rent = rentRepository.get(rentId);
        rent.endRent(endTime);
        rentRepository.update(rent);
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
            rent = Optional.ofNullable(entityManager.createQuery("SELECT r FROM Rent r WHERE r.id = :rentId", Rent.class)
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

    private int getMaxRentalsForClient(ClientType clientType) {
        if (clientType instanceof DiamondMembership) {
            return Integer.MAX_VALUE; // Bez limitu dla DiamondMembership
        } else if (clientType instanceof Membership) {
            return 5; // Limit dla zwykłego Membership
        } else if (clientType instanceof NoMembership) {
            return 2; // Limit dla NoMembership
        }
        return 0; // Domyślnie brak możliwości wypożyczania
    }

    public long countActiveRentsByClient(Client client, EntityManager entityManager) {
        String query = "SELECT COUNT(r) FROM Rent r WHERE r.client = :client AND r.endTime IS NULL";

        entityManager.lock(client, LockModeType.PESSIMISTIC_WRITE);

        return entityManager.createQuery(query, Long.class)
                .setParameter("client", client)
                .getSingleResult();
    }
}
