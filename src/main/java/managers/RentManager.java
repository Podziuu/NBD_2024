package managers;

import models.Client;
import models.Item;
import models.Rent;
import org.bson.types.ObjectId;
import repos.RentRepository;

import java.time.LocalDateTime;

public class RentManager {
    private final RentRepository rentRepository;
    ClientManager clientManager;

    public RentManager(RentRepository rentRepository, ClientManager clientManager) {
        this.rentRepository = rentRepository;
        this.clientManager = clientManager;
    }

    public void rentItem(LocalDateTime beginTime, Client client, Item item) {
        int max = client.getClientType().getMaxArticles();
        int rented = client.getRentsCount();
        if (rented >= max) {
            throw new IllegalArgumentException("Client has reached maximum number of rented items");
        }
        Rent rent = new Rent(beginTime, client, item);
        // TODO: Zmienic zeby item nie byl available
        rentRepository.addRent(rent);
        clientManager.addRent(client.getId(), rent.getId());
    }

    public void returnItem(ObjectId rentId, Item item) {
        Rent rent = rentRepository.getRent(rentId);
        rent.endRent(LocalDateTime.now());
        rent.setArchive(true);
        rentRepository.updateRent(rent);
        clientManager.removeRent(rent.getClient().getId(), rent.getId());
        // TODO: Zmienic zeby item byl available
    }

    public void removeRent(ObjectId rentId) {
        Rent rent = rentRepository.getRent(rentId);
        rentRepository.removeRent(rentId);
        clientManager.removeRent(rent.getClient().getId(), rent.getId());
    }

    public Rent getRent(ObjectId rentId) {
        return rentRepository.getRent(rentId);
    }

}