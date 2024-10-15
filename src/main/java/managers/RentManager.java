package managers;

import exceptions.ParameterException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import models.*;
import repos.RentRepository;

import java.time.LocalDateTime;
import java.util.List;

public class RentManager {

    private EntityManagerFactory entityManagerFactory;
    private RentRepository rentRepository;
    private ClientTypeManager clientTypeManager;

    public RentManager() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
        this.rentRepository = new RentRepository();
        this.clientTypeManager = new ClientTypeManager();
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
       return rentRepository.getActiveRentsByClient(client);
    }

    public Rent getRent(long rentId) {
        return rentRepository.get(rentId);
    }

    private int getMaxRentalsForClient(ClientType clientType) {
        return clientType.getMaxArticles();
    }

    public long countActiveRentsByClient(Client client, EntityManager entityManager) {
        String query = "SELECT COUNT(r) FROM Rent r WHERE r.client = :client AND r.endTime IS NULL";

        return entityManager.createQuery(query, Long.class)
                .setParameter("client", client)
                .getSingleResult();
    }
}
