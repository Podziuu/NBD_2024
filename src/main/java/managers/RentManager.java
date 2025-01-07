package managers;

import models.Client;
import models.ClientType;
import models.Item;
import models.Rent;
import repos.IRentRepository;
import repos.RentRepository;

import java.time.Instant;
import java.util.UUID;

public class RentManager {
    private final RentRepository rentRepository;
    private final ClientManager clientManager;
    private final ItemManager itemManager;

    public RentManager(RentRepository rentRepository, ClientManager clientManager, ItemManager itemManager) {
        this.rentRepository = rentRepository;
        this.clientManager = clientManager;
        this.itemManager = itemManager;
    }

    public UUID rentItem(Instant beginTime, UUID clientId, UUID itemId) {
        Client client = clientManager.getClient(clientId);
        Item item = itemManager.getItem(itemId);
        ClientType clientType = client.getClientType();

        int max = clientType.getMaxArticles();
        // TODO: Zrobic w repo metode ktora zwroci nam ile dany klient ma rentow
//        int rented = client.getRentsCount();
//
//        if (rented >= max) {
//            throw new IllegalArgumentException("Client has reached the maximum number of rented items.");
//        }

        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is already rented out.");
        }

        try {
            itemManager.setUnavailable(item.getId());
            Rent rent = new Rent(UUID.randomUUID(), beginTime, client.getId(), item.getId());
            rentRepository.addRent(rent);
//            clientManager.addRent(client.getId(), rent.getId());
            return rent.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to rent item: " + e.getMessage(), e);
        }
    }



    public void returnItem(UUID rentId) {
        Rent rent = rentRepository.getRentByRentId(rentId);
        if (rent == null) {
            throw new IllegalArgumentException("Rent not found.");
        }

        try {
            itemManager.setAvailable(rent.getItemId());
            rent.setEndTime(Instant.now());
            rent.setArchive(true);
            rentRepository.updateRent(rent);
//            clientManager.removeRent(rent.getClientId(), rent.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to return item: " + e.getMessage(), e);
        }
    }

    public void removeRent(UUID rentId) {
        Rent rent = rentRepository.getRentByRentId(rentId);
        if (rent == null) {
            throw new IllegalArgumentException("Rent not found.");
        }

        rentRepository.removeRent(rentId);
//        clientManager.removeRent(rent.getClientId(), rent.getId());
    }

    public Rent getRent(UUID rentId) {
        Rent rent = rentRepository.getRentByRentId(rentId);
        if (rent == null) {
            throw new IllegalArgumentException("Rent not found.");
        }
        return rent;
    }
}